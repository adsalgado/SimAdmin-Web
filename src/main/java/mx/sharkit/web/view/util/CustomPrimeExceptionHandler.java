package mx.sharkit.web.view.util;

import java.io.IOException;
import java.util.Map;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import org.primefaces.application.exceptionhandler.ExceptionInfo;
import org.primefaces.application.exceptionhandler.PrimeExceptionHandler;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Adrián Salgado
 */
public class CustomPrimeExceptionHandler extends PrimeExceptionHandler {

    public CustomPrimeExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    @Override
    protected void handleRedirect(FacesContext context, Throwable rootCause, ExceptionInfo info, boolean responseResetted) throws IOException {
        ExternalContext externalContext = context.getExternalContext();
        externalContext.getSessionMap().put(ExceptionInfo.ATTRIBUTE_NAME, info);

        Map errorPages = RequestContext.getCurrentInstance().getApplicationContext().getConfig().getErrorPages();
        String errorPage = evaluateErrorPage(errorPages, rootCause);

        String url = externalContext.getRequestContextPath() + errorPage;

        // workaround for mojarra -> mojarra doesn't reset PartialResponseWriter#inChanges if we call externalContext#resetResponse
        if (responseResetted && context.getPartialViewContext().isAjaxRequest()) {
            PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
            externalContext.addResponseHeader("Content-Type", "text/xml; charset=" + externalContext.getResponseCharacterEncoding());
            externalContext.addResponseHeader("Cache-Control", "no-cache");
            externalContext.setResponseContentType("text/xml");

            writer.write("\n");
            writer.startElement("partial-response", null);
            writer.startElement("redirect", null);
            writer.writeAttribute("url", url, null);
            writer.endElement("redirect");
            writer.endElement("partial-response");
        } else {
            // workaround for IllegalStateException from redirect of committed response
            if (externalContext.isResponseCommitted() && !context.getPartialViewContext().isAjaxRequest()) {
                PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();
                writer.startElement("script", null);
                writer.write("window.location.href = '" + url + "';");
                writer.endElement("script");
                writer.getWrapped().endDocument();
            } else {
                externalContext.redirect(url);
            }
        }

        context.responseComplete();
    }

    @Override
    protected String evaluateErrorPage(Map errorPages, Throwable rootCause) {

        // get error page by exception type
        String errorPage = (String) errorPages.get(rootCause.getClass().getName());

        // lookup by inheritance hierarchy
        if (errorPage == null) {
            Class throwableClass = rootCause.getClass();
            while (errorPage == null && throwableClass.getSuperclass() != Object.class) {
                throwableClass = throwableClass.getSuperclass();
                errorPage = (String) errorPages.get(throwableClass.getName());
            }
        }

        // get default error page
        if (errorPage == null) {
            errorPage = (String) errorPages.get(null);
        }

        if (errorPage == null) {
            errorPage = "/customError";
//            throw new IllegalArgumentException(
//                    "No default error page (Status 500 or java.lang.Throwable) and no error page for type \"" + rootCause.getClass() + "\" defined!");
        }

        return errorPage;
    }

}
