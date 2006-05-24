package com.thoughtworks.frankenstein.common;

import java.awt.*;
import javax.swing.*;

/**
 * Decodes text from JLabels.
 * @author Vivek Prahlad
 */
public class JLabelDecoder implements RendererDecoder{
    public String decode(Component renderer) {
        return ((JLabel) renderer).getText();
    }
}
