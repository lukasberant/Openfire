package org.jivesoftware.openfire.plugin.rest.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jivesoftware.openfire.plugin.rest.entity.UnpairDevicesOfFriendsObject;
/**
 * The Class ItemsToUnpairContainer.
 */
@XmlRootElement(name = "itemsToUnpairContainer")
@XmlType(propOrder = { "items" })
public class ItemsToUnpairContainer {

    /** The groups. */
    private List<UnpairDevicesOfFriendsObject> items;


    /**
     * Instantiates a new roster item entity.
     */
    public ItemsToUnpairContainer(List<UnpairDevicesOfFriendsObject> items) {
        this.items = items;
   
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    @XmlElement(name = "items")
    @XmlElementWrapper(name = "items")
    public List<UnpairDevicesOfFriendsObject> getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items
     *            the new items
     */
    public void setItems(List<UnpairDevicesOfFriendsObject> items) {
        this.items = items;
    }
}
