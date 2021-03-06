package org.jboss.as.quickstarts.ejbinwar.enums;

public enum UpdateMessageType {

    /** successful action messages**/

    ORDER_CREATED,
    ORDER_EDITED,
    ORDER_DELETED,

    DRIVER_CREATED,
    DRIVER_EDITED,
    DRIVER_DELETED,

    MANAGER_CREATED,
    MANAGER_EDITED,
    MANAGER_DELETED,

    ADMIN_CREATED,
    ADMIN_EDITED,
    ADMIN_DELETED,

    USER_CREATED,
    USER_EDITED,
    USER_DELETED,


    TRUCK_CREATED,
    TRUCK_EDITED,
    TRUCK_DELETED,


    TRUCK_FIELDS_UPDATED,

    /** error messages **/
    // todo: add error messages

    // orders
    ERROR_ORDER_DTO_IS_NOT_VALID,

    ERROR_CAN_NOT_PARSE_ORDER_ID,


    ERROR_CAN_NOT_CREATE_ORDER,

    ERROR_CAN_NOT_UPDATE_ORDER,

    ERROR_NO_ORDER_WITH_THIS_ID,

    ERROR_CAN_NOT_DELETE_ORDER_WITH_SUCH_STATUS,

    // trucks
    ERROR_NO_TRUCK_WITH_THIS_ID,

    // drivers
    ERROR_CAN_NOT_PARSE_DRIVER_ID,

    ERROR_DRIVER_HOURS_WORKED_OVER_LIMIT,

    ERROR_DRIVER_DTO_IS_NOT_VALID,

    ERROR_CAN_NOT_UPDATE_DRIVER,

    ERROR_USER_IS_NOT_A_DRIVER,

    //managers

    ERROR_MANAGER_DTO_IS_NOT_VALID,
    ERROR_CAN_NOT_UPDATE_MANAGER,
    ERROR_USER_IS_NOT_A_MANAGER,
    // admins

    ERROR_ADMIN_DTO_IS_NOT_VALID,
    ERROR_CAN_NOT_UPDATE_ADMIN,
    ERROR_USER_IS_NOT_AN_ADMIN,
    // users

    ERROR_NO_USER_WITH_THIS_ID,

    ERROR_USER_DTO_IS_NOT_VALID,
    ERROR_CAN_NOT_PARSE_USER_ID,

    ERROR_CAN_NOT_CREATE_USER,
    ERROR_CAN_NOT_EDIT_USER,

    ERROR_CAN_NOT_DELETE_USER,
    // trucks

    ERROR_CAN_NOT_PARSE_NUM_OF_DRIVERS_AND_CAPACITY,
    ERROR_TRUCK_DTO_IS_NOT_VALID,

    ERROR_CAN_NOT_PARSE_TRUCK_ID,
    ERROR_CAN_NOT_PARSE_NUM_OF_DRIVERS,
    ERROR_CAN_NOT_PARSE_CAPACITY,


    ERROR_CAN_NOT_EDIT_TRUCK_WITH_ASSIGNED_ORDER,

    ERROR_NUM_OF_DRIVERS_TO_ASSIGN_MORE_THAN_MAXIMAL_FOR_THIS_TRUCK,

    ERROR_NEW_NUM_OF_DRIVERS_LESS_THAN_NUM_OF_CURRENT_ASSIGNED_DRIVERS,
    ERROR_CAN_NOT_UPDATE_TRUCK,


    ERROR_CAN_NOT_DELETE_TRUCK_WITH_EXEC_ORDER,
    // for all

    ERROR_ID_IS_NOT_VALID

}
