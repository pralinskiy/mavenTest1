package client;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;

public class DocArea implements Document {
    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {

    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {

    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {

    }

    @Override
    public void removeUndoableEditListener(UndoableEditListener listener) {

    }

    @Override
    public Object getProperty(Object key) {
        return null;
    }

    @Override
    public void putProperty(Object key, Object value) {

    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {

    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        return null;
    }

    @Override
    public void getText(int offset, int length, Segment txt) throws BadLocationException {

    }

    @Override
    public Position getStartPosition() {
        return null;
    }

    @Override
    public Position getEndPosition() {
        return null;
    }

    @Override
    public Position createPosition(int offs) throws BadLocationException {
        return null;
    }

    @Override
    public Element[] getRootElements() {
        return new Element[0];
    }

    @Override
    public Element getDefaultRootElement() {
        return null;
    }

    @Override
    public void render(Runnable r) {

    }
}
