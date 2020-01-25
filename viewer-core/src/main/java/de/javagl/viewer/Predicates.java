/*
 * www.javagl.de - Viewer
 *
 * Copyright (c) 2013-2015 Marco Hutter - http://www.javagl.de
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.viewer;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Methods to create predicate instances.<br>
 * <br>
 * The main purpose of these methods is to create predicates that offer 
 * a human-readable and sensible <code>toString</code> representation.<br>
 * <br>
 * This class is not part of the public API, and may be omitted in the future.
 */
public class Predicates
{
    /**
     * Create a predicate with the given string representation, that
     * has its <code>and</code>, <code>or</code> and <code>negate</code>
     * methods implemented by delegating to the methods of this class.
     *  
     * @param p The input predicate. May not be <code>null</code>.
     * @param string The string representation of the predicate
     * @return The predicate
     */
    public static <T> Predicate<T> create(Predicate<? super T> p, String string)
    {
        Objects.requireNonNull(p);
        return new Predicate<T>()
        {
            @Override
            public boolean test(T t)
            {
                return p.test(t);
            }
            
            @Override
            public Predicate<T> and(Predicate<? super T> other) 
            {
                return Predicates.and(this, other);
            }

            @Override
            public Predicate<T> negate() 
            {
                return Predicates.negate(this);
            }

            @Override
            public Predicate<T> or(Predicate<? super T> other) 
            {
                return Predicates.or(this, other);
            }
            
            @Override
            public String toString()
            {
                return string;
            }
        };
    }
    
    /**
     * Returns a predicate that is a short-circuiting conjunction of
     * the given predicates, and whose string representation resembles
     * the code of this condition.
     * 
     * @param p0 The first predicate. May not be <code>null</code>.
     * @param p1 The second predicate. May not be <code>null</code>.
     * @return The resulting predicate
     */
    public static <T> Predicate<T> and(
        Predicate<? super T> p0, 
        Predicate<? super T> p1)
    {
        Objects.requireNonNull(p0);
        Objects.requireNonNull(p1);
        return new Predicate<T>()
        {
            @Override
            public boolean test(T t)
            {
                return p0.test(t) && p1.test(t);
            }
            
            @Override
            public String toString()
            {
                return "(" + p0 + " && " + p1 + ")";
            }
        };
    }

    /**
     * Returns a predicate that is a short-circuiting disjunction of
     * the given predicates, and whose string representation resembles
     * the code of this condition.
     * 
     * @param p0 The first predicate. May not be <code>null</code>.
     * @param p1 The second predicate. May not be <code>null</code>.
     * @return The resulting predicate
     */
    public static <T> Predicate<T> or(
        Predicate<? super T> p0, 
        Predicate<? super T> p1)
    {
        Objects.requireNonNull(p0);
        Objects.requireNonNull(p1);
        return new Predicate<T>()
        {
            @Override
            public boolean test(T t)
            {
                return p0.test(t) || p1.test(t);
            }
            
            @Override
            public String toString()
            {
                return "(" + p0 + " || " + p1 + ")";
            }
        };
    }

    /**
     * Returns a predicate that is a negation of the given predicate, and 
     * whose string representation resembles the code of this condition.
     * 
     * @param p The predicate. May not be <code>null</code>.
     * @return The resulting predicate
     */
    public static <T> Predicate<T> negate(
        Predicate<? super T> p)
    {
        Objects.requireNonNull(p);
        return new Predicate<T>()
        {
            @Override
            public boolean test(T t)
            {
                return !p.test(t);
            }
            
            @Override
            public String toString()
            {
                return "!(" + p + ")";
            }
        };
    }
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private Predicates()
    {
        // Private constructor to prevent instantiation
    }
}

