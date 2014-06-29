package com.google.cloud.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.cloud.client.objects.BaseElement;

/**
 * Utility class providing methods to compare objects.
 */
public class ComparationUtils {

	/**
	 * Compares the two given Lists and returns true, if they are equal and
	 * false, if they are not. Optionally, it is able to compare the order, too.
	 * 
	 * @param a
	 *            one of the two lists to compare
	 * @param b
	 *            the other one of the two lists to compare
	 * @param compareOrder
	 *            if the order of the lists should be compared, too (optional)
	 * @return if the two given lists are equal.
	 */
	public static <T> boolean areListsEqual(List<T> a, List<T> b,
			boolean compareOrder) {

		if ((a == null && b == null))
			return true;
		if ((a == null && b != null) || (a != null && b == null))
			return false;

		int aSize = a.size();
		int bSize = b.size();

		if (aSize != bSize)
			return false;

		if (compareOrder) {

			for (int i = 0; i < aSize; i++) {
				if (a.get(i) == null) {
					if (b.get(i) != null)
						return false;
				} else {
					if (!a.get(i).equals(b.get(i)))
						return false;
				}
			}

		} else {
			if (!internalAreCollectionsEqual(a, b, aSize))
				return false;
		}

		return true;
	}

	/**
	 * Compares two collections without giving credit to the order.
	 * 
	 * @param a
	 *            one of the two lists to compare
	 * @param b
	 *            the other one of the two lists to compare
	 * @return if the two given lists are equal.
	 */
	public static <T> boolean areCollectionsEqual(Collection<T> a,
			Collection<T> b) {

		if ((a == null && b == null))
			return true;
		if ((a == null && b != null) || (a != null && b == null))
			return false;

		int aSize = a.size();
		int bSize = b.size();

		if (aSize != bSize)
			return false;

		return internalAreCollectionsEqual(a, b, aSize);
	}

	private static <T> boolean internalAreCollectionsEqual(Collection<T> a,
			Collection<T> b, int size) {

		List<T> aCopy, bCopy;
		aCopy = new ArrayList<T>(a);
		bCopy = new ArrayList<T>(b);

		for (int i = 0; i < size; i++) {
			T item = aCopy.get(i);
			if (!bCopy.remove(item))
				return false;
		}

		if (!bCopy.isEmpty())
			return false;

		return true;
	}

	/**
	 * Calls <T> boolean areCollectionsEqual(List<T> a, List<T> b) with the
	 * given parameters a and b.
	 * 
	 * @param a
	 *            one of the two lists to compare
	 * @param b
	 *            the other one of the two lists to compare
	 * @return if the two given lists are equal.
	 */
	public static <T> boolean areListsEqual(List<T> a, List<T> b) {

		return areCollectionsEqual(a, b);
	}

	/**
	 * Compares the two given objects and returns true, if they are equal and
	 * false, if they are not.
	 * 
	 * @param a
	 *            one of the two objects to compare
	 * @param b
	 *            the other one of the two objects to compare
	 * @return if the two given lists are equal.
	 */
	public static boolean areObjectsEqual(Object a, Object b) {

		if (a == null && b == null)
			return true;
		if (a == null || b == null)
			return false;
		return a.equals(b);
	}
	/**
	 * Returns The first index of an UBIKObject in a list based on its UID or -1, if the object wasn't found.
	 * 
	 * @param list
	 *            The list of objects to search in.
	 * @param object
	 *            The object, which'S UID should be compared.
	 * @return The first index of the list, where an object with the same UID resides or -1, if it wasn't found.
	 */
	public static <T extends BaseElement> int uidBasedIndexOf(List<T> list, T object) {

		int index = -1;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				T toCompare = list.get(i);
				if (toCompare != null && object != null) {
					if (areObjectsEqual(object.getElementId(), toCompare.getElementId())) {
						index = i;
						break;
					}
				} else if (object == null && toCompare == null) {
					index = i;
					break;
				}
			}
		}
		return index;
	}
}