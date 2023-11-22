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

import java.util.Objects;

/**
 * Pair represents a pair of objects
 *
 * @param <T> the type of the first object
 * @param <S> the type of the second object
 * @author gp
 */
public class Pair<T, S>
{
	/**
	 * x is the first element of the pair
	 */
	private T x;
	/**
	 * y is the second element of the pair
	 */
	private S y;

	/**
	 * Create a Pair from two values
	 *
	 * @param x the first value
	 * @param y the second value
	 */
	public Pair(T x, S y)
	{
		this.x = x;
		this.y = y;
	}


	/**
	 * get first value
	 *
	 * @return the first value
	 */
	public T getX()
	{
		return x;
	}

	/**
	 * get second value
	 *
	 * @return the second value
	 */
	public S getY()
	{
		return y;
	}

	/**
	 * We must provide some way to allow subclasses to set the x attribute, so
	 * it's protected.
	 *
	 * @param x set the first value
	 */
	protected void setX(T x)
	{
		this.x = x;
	}

	/**
	 * We must provide some way to allow subclasses to set the y attribute, so
	 * it's protected.
	 *
	 * @param y set the second value
	 */
	protected void setY(S y)
	{
		this.y = y;
	}

	@Override
	public String toString()
	{
		return "Pair(x=" + x + ", y=" + y + ")";
	}

	/*
  @Override
  public boolean equals(Object obj)
  {
		if (obj == null)
			return false;

    if (!(obj instanceof Pair pair))
      return false;

    return pair.y.equals(y) && pair.x.equals(x);
  }

	@Override
	public int hashCode()
	{
		return Objects.hashCode(x) + Objects.hashCode(y) * 29;
	}
	*/
}

/* vim: set ts=2 sw=2 tw=0 noet :*/
