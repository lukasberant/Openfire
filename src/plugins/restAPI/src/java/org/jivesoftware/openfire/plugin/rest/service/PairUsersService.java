package org.jivesoftware.openfire.plugin.rest.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.plugin.rest.controller.UserServiceController;
import org.jivesoftware.openfire.plugin.rest.entity.RosterItemEntity;
import org.jivesoftware.openfire.plugin.rest.entity.ItemToPairEntities;
import org.jivesoftware.openfire.plugin.rest.entity.ItemToPairEntity;
import org.jivesoftware.openfire.plugin.rest.entity.ItemToUnpairEntities;
import org.jivesoftware.openfire.plugin.rest.entity.ItemToUnpairEntity;
import org.jivesoftware.openfire.plugin.rest.exceptions.ExceptionType;
import org.jivesoftware.openfire.plugin.rest.exceptions.ServiceException;
import org.jivesoftware.openfire.user.UserAlreadyExistsException;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.roster.RosterItem;

@Path("restapi/v1/users/roster/multipairing")
public class PairUsersService {

    private static final String COULD_NOT_CREATE_ROSTER_ITEM = "Could not create roster item";

    private UserServiceController plugin;

    @PostConstruct
    public void init() {
        plugin = UserServiceController.getInstance();
    }

    @POST
    public Response pairUsers(List<ItemToPairEntity> items)   
            throws ServiceException {
        try {
            for (ItemToPairEntity item :  items) {
                RosterItemEntity user2RosterEntity = new RosterItemEntity(item.getUser2DeviceXmppLogin(), item.getUser2DeviceName(), RosterItem.SubType.BOTH.getValue());
                List<String> user2RosterEntiryGroups = new ArrayList<String>();
                user2RosterEntiryGroups.add(item.getUser2GroupName());
                user2RosterEntity.setGroups(user2RosterEntiryGroups);
                
                RosterItemEntity user1RosterEntity = new RosterItemEntity(item.getUser1DeviceXmppLogin(), item.getUser1DeviceName(), RosterItem.SubType.BOTH.getValue());
                List<String> user1RosterEntityGroups = new ArrayList<String>();
                user1RosterEntityGroups.add(item.getUser1GroupName());
                user1RosterEntity.setGroups(user1RosterEntityGroups);
        
                plugin.addOrUpdateRosterItem(item.getUser1DeviceXmppLogin(), user2RosterEntity);
                plugin.addOrUpdateRosterItem(item.getUser2DeviceXmppLogin(), user1RosterEntity);
            }
        } catch (UserNotFoundException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION,
                    Response.Status.NOT_FOUND, e);
        } catch (Exception e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", e.getMessage(),
                    Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    public Response unpairUsers(List<ItemToUnpairEntity> items)
            throws ServiceException {         
        try {
            for (ItemToUnpairEntity item :  items) {
                plugin.deleteRosterItem(item.getUser1DeviceXmppLogin(), item.getUser2DeviceXmppLogin());
                plugin.deleteRosterItem(item.getUser2DeviceXmppLogin(), item.getUser1DeviceXmppLogin());
            }
        } catch (SharedGroupException e) {
            throw new ServiceException("Could not delete the roster item", "",
                    ExceptionType.SHARED_GROUP_EXCEPTION, Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.OK).build();
    }

}
