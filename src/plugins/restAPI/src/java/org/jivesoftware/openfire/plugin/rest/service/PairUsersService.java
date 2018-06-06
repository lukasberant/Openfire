package org.jivesoftware.openfire.plugin.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.PostConstruct;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jivesoftware.openfire.SharedGroupException;
import org.jivesoftware.openfire.plugin.rest.controller.UserServiceController;
import org.jivesoftware.openfire.plugin.rest.entity.RosterEntities;
import org.jivesoftware.openfire.plugin.rest.entity.RosterItemEntity;
import org.jivesoftware.openfire.plugin.rest.entity.UnpairDevicesOfFriendsObject;
import org.jivesoftware.openfire.plugin.rest.entity.ItemsToPairContainer;
import org.jivesoftware.openfire.plugin.rest.entity.ItemsToUnpairContainer;
import org.jivesoftware.openfire.plugin.rest.entity.PairDevicesOfFriendsObject;
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
    public Response pairUsers(@PathParam("itemsToPairContainer") ItemsToPairContainer itemsToPairContainer)   
            throws ServiceException {
        try {
          if (itemsToPairContainer != null) {
                for (PairDevicesOfFriendsObject item :  itemsToPairContainer.getItems()) {
                    RosterItemEntity entity1 = new RosterItemEntity(item.getUser2DeviceXmppLogin(), item.getUser2DeviceName(), RosterItem.SubType.BOTH.ordinal());
                    List<String> groups1 = new ArrayList<String>();
                    groups1.add(item.getUser2GroupName());
                    entity1.setGroups(groups1);
                    
                    RosterItemEntity entity2 = new RosterItemEntity(item.getUser1DeviceXmppLogin(), item.getUser1DeviceName(), RosterItem.SubType.BOTH.ordinal());
                    List<String> groups2 = new ArrayList<String>();
                    groups2.add(item.getUser1GroupName());
                    entity2.setGroups(groups2);
        
                    
                    // username,
                    // pairUsername,
                    // nickname,
                    // group,

            
                    plugin.addRosterItem(item.getUser1DeviceXmppLogin(), entity1);
                    plugin.addRosterItem(item.getUser2DeviceName(), entity2);
            
                }
          }
        } catch (UserNotFoundException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_NOT_FOUND_EXCEPTION,
                    Response.Status.NOT_FOUND, e);
        } catch (UserAlreadyExistsException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.USER_ALREADY_EXISTS_EXCEPTION,
                    Response.Status.CONFLICT, e);
        } catch (SharedGroupException e) {
            throw new ServiceException(COULD_NOT_CREATE_ROSTER_ITEM, "", ExceptionType.SHARED_GROUP_EXCEPTION,
                    Response.Status.BAD_REQUEST, e);
        }
         return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    public Response unpairUsers(ItemsToUnpairContainer itemsToUnpairContainer)
            throws ServiceException {
                String rosterJid = "";
         
        try {
            for (UnpairDevicesOfFriendsObject item :  itemsToUnpairContainer.getItems()) {
                rosterJid = item.getUser2DeviceXmppLogin();
                plugin.deleteRosterItem(item.getUser1DeviceXmppLogin(), rosterJid);
                rosterJid = item.getUser1DeviceXmppLogin();
                plugin.deleteRosterItem(item.getUser2DeviceXmppLogin(), rosterJid);
            }

        } catch (SharedGroupException e) {
            throw new ServiceException("Could not delete the roster item", rosterJid,
                    ExceptionType.SHARED_GROUP_EXCEPTION, Response.Status.BAD_REQUEST, e);
        }
        return Response.status(Response.Status.OK).build();
    }

}
