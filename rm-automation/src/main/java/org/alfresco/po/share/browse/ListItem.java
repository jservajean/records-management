/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.share.browse;

import static org.alfresco.po.common.util.Utils.waitForStalenessOf;
import static org.alfresco.po.common.util.Utils.waitForVisibilityOf;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.po.common.renderable.Renderable;
import org.alfresco.po.common.util.Utils;
import org.alfresco.po.share.page.SharePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import ru.yandex.qatools.htmlelements.element.Link;

/**
 * List item
 * 
 * @author Roy Wetherall
 */
public abstract class ListItem
{
    /** action(s) selector */
    private static final By ACTIONS_SELECTOR = By.cssSelector("div.action-set div");
    private static final String ACTION_SELECTOR_XPATH = ".//div[@class=''{0}'']/a";

    /** link selector */
    private static By linkSelector = By.xpath(".//td[contains(@class,'fileName')]//h3//a");

    /** more actions */
    private static By moreActionsSelector = By.xpath(".//div[@class='internal-show-more']/a");
    private static By moreActionsPanelSelector = By.xpath(".//div[starts-with(@class,'more-actions')]");

    /** data row */
    private WebElement row;

    /**
     * @param row row element
     */
    public void setRow(WebElement row)
    {
        this.row = row;
    }

    /**
     * @return  row element
     */
    protected WebElement getRow()
    {
        return row;
    }
    
    /**
     * Helper method to get link
     */
    protected Link getLink()
    {
        return new Link(row.findElement(linkSelector));
    }

    /**
     * @return get name
     */
    public String getName()
    {
        return getLink().getText();
    }

    /**
     * Click on link
     */
    public Renderable clickOnLink()
    {
        return clickOnLink(SharePage.getLastRenderedPage());
    }

    /**
     * Click on link
     */
    public <T extends Renderable> T clickOnLink(T renderable)
    {
        // get the link
        Link link = getLink();

        // mouse over the link
        Utils.mouseOver(link);
        
        // click on the link
        link.click();

        // wait for the link to become stale
        waitForStalenessOf(link);

        // render and return the file plan
        return renderable.render();
    }

    /**
     * Click on the more actions link
     */
    private void clickOnMoreActions()
    {
        // click on the ...more link
        Utils.mouseOver(row);    
        if (Utils.elementExists(row, moreActionsSelector))
        {
            // find the element, mouse over and click
            WebElement moreAction = row.findElement(moreActionsSelector);
            Utils.mouseOver(moreAction);
            moreAction.click();
            
            // wait for the actions to show
            waitForVisibilityOf(row.findElement(moreActionsPanelSelector));
        }
    }

    /**
     * Helper method to check whether the specified actions are clickable
     * 
     * @param actionNames action names
     * @return boolean true if all clickable, false otherwise
     */
    public List<String> isActionsClickable(String... actionNames)
    {
        List<String> result = null;

        // click on more actions
        clickOnMoreActions();

        // check each action
        for (String actionName : actionNames)
        {
            Link link = getActionLink(actionName);
            if (link == null || !link.isEnabled())
            {
                if (result == null)
                {
                    result = new ArrayList<String>();
                }                
                result.add(actionName);
            }
        }

        return result;
    }
    
    public String[] getClickableActions()
    {
        // click on more actions
        clickOnMoreActions();
        
        List<WebElement> actions = row.findElements(ACTIONS_SELECTOR);
        List<String> result = new ArrayList<String>();
        for (WebElement action : actions)
        {
            String actionName = action.getAttribute("class");
            if (!actionName.contains("internal-show-more") &&
                !actionName.contains("more-actions"))
            {
                result.add(actionName);
            }
        }
        
        return result.toArray(new String[result.size()]);
    }

    /**
     * Is action clickable
     */
    public boolean isActionClickable(String actionName)
    {
        boolean result = false;
        
        // click on more actions
        clickOnMoreActions();
        
        // get the link and check it's enabled
        Link link = getActionLink(actionName);
        if (link != null && link.isEnabled())
        {
            result = true;
        }
        return result;
    }

    /**
     * Click on action
     */
    public Renderable clickOnAction(String actionName)
    {
        return clickOnAction(actionName, SharePage.getLastRenderedPage(), true);
    }

    /**
     * Click on dialog action
     */
    public <T extends Renderable> T clickOnAction(String actionName, T renderable)
    {
        return clickOnAction(actionName, renderable, false);
    }
    
    /**
     * Click on action
     */
    private <T extends Renderable> T clickOnAction(String actionName, T renderable, boolean waitForActionStaleness)
    {
        // click on more actions
        clickOnMoreActions();

        // get action link
        Link action = getActionLink(actionName);
        if (action == null || !action.isEnabled())
            throw new RuntimeException("The action " + actionName + " could not be found for list item " + getName());
        
        // mouse over and click
        //Utils.mouseOver(action);
        action.click();

        if (waitForActionStaleness == true)
        {            
            waitForStalenessOf(action);
        }

        // render the return page
        return renderable.render();
    }

    /**
     * Helper method to get the action link
     */
    private Link getActionLink(String actionName)
    {
        Link result = null;
        try
        {
            WebElement link = row.findElement(getActionSelector(actionName));
            result = new Link(link);
        }
        catch (NoSuchElementException e)
        {
            // do nothing, just return null
        }
        return result;
    }

    /**
     * Helper method to get the action selector
     */
    private By getActionSelector(String actionName)
    {
        String actionXPATH = MessageFormat.format(ACTION_SELECTOR_XPATH, actionName);
        return By.xpath(actionXPATH);
    }


}
