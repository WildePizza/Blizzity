package com.github.WildePizza.gui.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class SVGButton extends Pane {
    double height;
    double width;
    public boolean selected = false;
    private final Rectangle rectangle;
    private final Node group;
    public void setBackgroundColor(Color color) {
        rectangle.setFill(color);
    }
    private final ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "onAction";
        }
    };
    public SVGButton(Node group, double width, double height, double arc) {
        this.group = group;
        this.height = height;
        this.width = width;
        Rectangle hitbox = new Rectangle(0, 0, width, height);
        hitbox.setFill(javafx.scene.paint.Color.TRANSPARENT);

        double multiplier = Math.min(height, width)/40;

        rectangle = new Rectangle(width-10*multiplier, height-10*multiplier);
        rectangle.setFill(Color.WHITE);
        rectangle.setArcWidth(arc);
        rectangle.setArcHeight(arc);
        rectangle.setLayoutX(5*multiplier);
        rectangle.setLayoutY(5*multiplier);

        getChildren().addAll(group, hitbox);
        hitbox.setOnMouseEntered(event -> {
            if (!selected && !getChildren().contains(rectangle)) {
                getChildren().add(rectangle);
                group.toFront();
                hitbox.toFront();
            }
        });
        hitbox.setOnMouseExited(event -> {
            if (!selected && getChildren().contains(rectangle) && (!group.isHover() || !hitbox.isHover() || !rectangle.isHover())) {
                getChildren().remove(rectangle);
                group.toFront();
                hitbox.toFront();
            }

        });
        hitbox.setOnMouseClicked(event -> {
            onAction.getValue().handle(new ActionEvent());
            selected = true;
        });
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            if (!getChildren().contains(rectangle)) {
                getChildren().add(rectangle);
                group.toFront();
            }
        } else {
            getChildren().remove(rectangle);
        }

    }

    public final void setOnAction(EventHandler<ActionEvent> var1) {
        this.onAction.set(var1);
    }
}
