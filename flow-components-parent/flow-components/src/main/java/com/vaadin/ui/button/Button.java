/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.ui.button;

import com.vaadin.flow.dom.Element;
import com.vaadin.ui.Component;
import com.vaadin.ui.common.HasSize;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.html.Image;
import com.vaadin.ui.icon.Icon;

/**
 * Server-side component for the <code>vaadin-button</code> element.
 *
 * @author Vaadin Ltd
 */
public class Button extends GeneratedVaadinButton<Button> implements HasSize {

    private Component iconComponent;
    private boolean iconAfterText;

    /**
     * Default constructor. Creates an empty button.
     */
    public Button() {
    }

    /**
     * Creates a button with a text inside.
     * 
     * @param text
     *            the text inside the button
     * @see #setText(String)
     */
    public Button(String text) {
        setText(text);
    }

    /**
     * Creates a button with an icon inside.
     * 
     * @param icon
     *            the icon inside the button
     * @see #setIcon(Component)
     */
    public Button(Component icon) {
        setIcon(icon);
    }

    /**
     * Creates a button with a text and an icon inside.
     * 
     * @param text
     *            the text inside the button
     * @param icon
     *            the icon inside the button
     * @see #setText(String)
     * @see #setIcon(Component)
     */
    public Button(String text, Component icon) {
        setIcon(icon);
        setText(text);
    }

    /**
     * Creates a button with a text and a listener for click events.
     * 
     * @param text
     *            the text inside the button
     * @param clickListener
     *            the event listener for click events
     * @see #setText(String)
     * @see #addClickListener(ComponentEventListener)
     */
    public Button(String text,
            ComponentEventListener<ClickEvent<Button>> clickListener) {
        setText(text);
        addClickListener(clickListener);
    }

    /**
     * Creates a button with an icon and a listener for click events.
     * 
     * @param icon
     *            the icon inside the button
     * @param clickListener
     *            the event listener for click events
     * @see #setIcon(Component)
     * @see #addClickListener(ComponentEventListener)
     */
    public Button(Component icon,
            ComponentEventListener<ClickEvent<Button>> clickListener) {
        setIcon(icon);
        addClickListener(clickListener);
    }

    /**
     * Sets the given string as the text content of this component.
     * <p>
     * This method removes any existing text nodes from this component and adds
     * a new text node with the given content.
     *
     * @param text
     *            the text content to set, may be <code>null</code> to only
     *            remove existing text
     */
    @Override
    public void setText(String text) {
        getElement().removeChild(getTextNodes());

        if (text == null) {
            return;
        }

        if (iconComponent != null && !iconAfterText) {
            getElement().appendChild(Element.createText(text));
        } else {
            getElement().insertChild(0, Element.createText(text));
        }
    }

    /**
     * Sets the given component as the icon of this button.
     * <p>
     * Even though you can use almost any component as an icon, some good
     * options are {@link Icon} and {@link Image}.
     * 
     * @param icon
     *            component to be used as an icon, may be <code>null</code> to
     *            only remove the current icon, can't be a text-node
     * @see #setIconAfterText(boolean)
     */
    public void setIcon(Component icon) {
        if (icon != null && icon.getElement().isTextNode()) {
            throw new IllegalArgumentException(
                    "Text node can't be used as an icon.");
        }
        if (iconComponent != null) {
            remove(iconComponent);
        }

        iconComponent = icon;
        if (iconComponent == null) {
            return;
        }

        if (iconAfterText) {
            add(iconComponent);
        } else {
            getElement().insertChild(0, iconComponent.getElement());
        }
    }

    /**
     * Gets the component that is defined as the icon of this button.
     * 
     * @return the icon of this button, or <code>null</code> if the icon is not
     *         set
     */
    public Component getIcon() {
        return iconComponent;
    }

    /**
     * Sets whether this button's icon should be positioned after it's text
     * content or the other way around.
     * <p>
     * This method reorders possibly existing icon and text content if needed
     * and also affects icon and text that are set afterwards.
     * 
     * @param iconAfterText
     *            whether the icon should be positioned after the text content
     *            or not
     */
    public void setIconAfterText(boolean iconAfterText) {
        if (this.iconAfterText == iconAfterText) {
            return;
        }
        this.iconAfterText = iconAfterText;

        Element[] textNodes = getTextNodes();
        if (iconComponent == null || textNodes.length == 0) {
            return;
        }

        // reordering text and icon if necessary
        int iconIndex = getElement().indexOfChild(iconComponent.getElement());
        if (iconAfterText) {
            int lastTextIndex = getElement()
                    .indexOfChild(textNodes[textNodes.length - 1]);
            if (iconIndex < lastTextIndex) {
                remove(iconComponent);
                getElement().insertChild(lastTextIndex,
                        iconComponent.getElement());
            }
        } else {
            int firstTextIndex = getElement().indexOfChild(textNodes[0]);
            if (iconIndex > firstTextIndex) {
                remove(iconComponent);
                getElement().insertChild(firstTextIndex,
                        iconComponent.getElement());
            }
        }
    }

    /**
     * Gets whether this button's icon is positioned after it's text content or
     * the other way around.
     * 
     * @return <code>true</code> if this button positions it's icon after it's
     *         text content, <code>false</code> otherwise
     */
    public boolean isIconAfterText() {
        return iconAfterText;
    }

    /**
     * Executes a click on this button at the client-side. Calling this method
     * behaves exactly the same as if the user would have clicked on the button.
     */
    public void click() {
        getElement().callFunction("click");
    }

    /**
     * Adds the given components as children of this component.
     * <p>
     * Note that using this method together with {@link #setText(String)},
     * {@link #setIcon(Component)} and {@link #setIconAfterText(boolean)} may
     * result in an unexpected order of child components.
     *
     * @param components
     *            the components to add
     */
    @Override
    public void add(Component... components) {
        super.add(components);
    }

    private Element[] getTextNodes() {
        return getElement().getChildren().filter(Element::isTextNode)
                .toArray(Element[]::new);
    }

}