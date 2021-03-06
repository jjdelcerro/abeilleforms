/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2001 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.editor;

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Action;

/**
 * Description of the annotation. The annotations is defined by AnnotationType,
 * length, offset and description.
 * 
 * @author David Konecny
 * @since 07/2001
 */
public abstract class AnnotationDesc extends Object {

	/** Property name of the tip text */
	public static final String PROP_SHORT_DESCRIPTION = "shortDescription";

	/** Property name of the annotation type */
	public static final String PROP_ANNOTATION_TYPE = "annotationType";

	/** Virtual property for fronting of annotation */
	public static final String PROP_MOVE_TO_FRONT = "moveToFront";

	/** Support for property change listeners */
	private PropertyChangeSupport support;

	/**
	 * This is sequential number which is used for cycling through the
	 * anotations. Each added annotion gets number and this number if necessary
	 * is used for cycling through the annotations - cycle causes that
	 * annotation with higher number is shown. If no higher number exist the
	 * cycling starts from the begining.
	 */
	private int order;

	/** Unique counter used for order variable. */
	private static int counter = 0;

	/** Length of the annotated text. If -1 than whole line is annotated */
	private int length;

	/**
	 * Private member used by Annotations class. After the annotation is
	 * attached to document, the Mark which is crated (or shared with some other
	 * annotation) is stored here. Only for internal purpose of Annoations class
	 */
	private Mark mark;

	/**
	 * AnnotationType attached to this annotation. Annotation has a few pass
	 * through methods to AnnotationType for simple access to the annotation
	 * type information.
	 */
	private AnnotationType type = null;

	public AnnotationDesc(int offset, int length) {
		counter++;
		this.order = counter;
		this.length = length;
		support = new PropertyChangeSupport(this);
	}

	/** Gets annotation coloring. This is pass through method to annotation type */
	public Coloring getColoring() {
		return type.getColoring();
	}

	/** Gets glyph image. This is pass through method to annotation type */
	public Image getGlyph() {
		return type.getGlyphImage();
	}

	/** Checks whether the annotation type has its own glyph icon */
	public boolean isDefaultGlyph() {
		return type.isDefaultGlyph();
	}

	/**
	 * Is annotation type visible. This is pass through method to annotation
	 * type
	 */
	public boolean isVisible() {
		return type.isVisible();
	}

	/** Internal order of the annotations. Used for correct cycling. */
	public int getOrderNumber() {
		return order;
	}

	/** Returns list of actions associated to this annotation type. */
	public Action[] getActions() {
		return type.getActions();
	}

	/** Whether this annotation annotates whole line or just part of the text */
	public boolean isWholeLine() {
		return length == -1;
	}

	/** Get length of the annotation */
	public int getLength() {
		return length;
	}

	/** Set Mark which represent this annotation in document */
	void setMark(Mark mark) {
		this.mark = mark;
	}

	/** Get Mark which represent this annotation in document */
	Mark getMark() {
		return mark;
	}

	/** Getter for annotation type object */
	public AnnotationType getAnnotationTypeInstance() {
		return type;
	}

	/** Getter for annotation type name */
	public abstract String getAnnotationType();

	/** Getter for localized tooltip text for this annotation */
	public abstract String getShortDescription();

	/** Getter for offset of this annotation */
	public abstract int getOffset();

	/** Getter for line number of this annotation */
	public abstract int getLine();

	/**
	 * Method for fetching AnnotationType which correspond to the name of the
	 * annotation type stored in annotation.
	 */
	public void updateAnnotationType() {
		type = AnnotationTypes.getTypes().getType(getAnnotationType());
	}

	/**
	 * Add listeners on changes of annotation properties
	 * 
	 * @param l
	 *            change listener
	 */
	final public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}

	/**
	 * Remove listeners on changes of annotation properties
	 * 
	 * @param l
	 *            change listener
	 */
	final public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}

	/** Fire property change to registered listeners. */
	final protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		support.firePropertyChange(propertyName, oldValue, newValue);
	}

	public String toString() {
		return "Annotation: type='" + getAnnotationType() + "', line=" + getLine() + // NOI18N
				", offset=" + getOffset() + ", length=" + length + // NOI18N
				", coloring=" + getColoring(); // NOI18N
	}

}
