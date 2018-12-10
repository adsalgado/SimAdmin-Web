/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.restObject;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author juan.lopez
 */
public class ObjectRequest {
    @Setter
    @Getter
    private String status;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private String idTransaction;
    @Setter
    @Getter
    private Object parameters;
    @Setter
    @Getter
    private String deviceId;
    @Setter
    @Getter
    private String timeRequest;

}
