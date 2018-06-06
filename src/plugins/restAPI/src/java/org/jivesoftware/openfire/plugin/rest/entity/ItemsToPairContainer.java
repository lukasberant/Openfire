package org.jivesoftware.openfire.plugin.rest.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jivesoftware.openfire.plugin.rest.entity.PairDevicesOfFriendsObject;

/**
 * The Class ItemsToPairContainer.
 */
@XmlRootElement(name = "itemsToPairContainer")
@XmlType(propOrder = { "items" })
public class ItemsToPairContainer {

    /** The groups. */
    private List<PairDevicesOfFriendsObject> items;


    /**
     * Instantiates a new roster item entity.
     */
    public ItemsToPairContainer(List<PairDevicesOfFriendsObject> items) {
        this.items = items;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    @XmlElement(name = "items")
    @XmlElementWrapper(name = "items")
    public List<PairDevicesOfFriendsObject> getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items
     *            the new items
     */
    public void setItems(List<PairDevicesOfFriendsObject> items) {
        this.items = items;
    }
}
