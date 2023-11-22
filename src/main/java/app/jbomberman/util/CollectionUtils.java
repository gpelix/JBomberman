/*
 * Copyright (c) 2023, Gabriele Pelissetto
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement:
 *      This product includes software developed by Gabriele Pelissetto
 *
 * 4. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package app.jbomberman.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * CollectionUtils is a utility class that provides some useful static functions
 * to work with collections
 *
 * @author gp
 */
public class CollectionUtils
{
	/**
	 * randomPickAndRemove() is a function that picks an element from a list,
	 * and it removes it from the list
	 *
	 * @param list the list from which the element must be picked
	 * @param <T>  the type of the elements in the list
	 * @return the element picked and removed
	 */
	public static <T> T randomPickAndRemove(List<T> list)
	{
		return list.remove(RandomNumberGenerator.getInstance().nextInt(list.size()));
	}

	/**
	 * randomPick() is a function that picks a random element from a list
	 *
	 * @param list the list from which the element must be picked
	 * @param <T>  the type of the elements in the list
	 * @return the element picked
	 */
	public static <T> T randomPick(List<T> list)
	{
		return list.get(RandomNumberGenerator.getInstance().nextInt(list.size()));
	}

	/**
	 * pick() is a function that picks a generic (not necessarily random)
	 * element from a collection
	 *
	 * @param collection the collection from which the element must be picked
	 * @param <T>        the type of the elements in the Collection
	 * @return the element picked
	 */
	public static <T> Maybe<T> pick(Collection<T> collection)
	{
		Iterator<T> iterator = collection.iterator();

		return Maybe.something(iterator.hasNext() ? iterator.next() : null);
	}
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
