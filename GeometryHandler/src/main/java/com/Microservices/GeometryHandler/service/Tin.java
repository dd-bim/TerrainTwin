package com.Microservices.GeometryHandler.service;

import java.util.HashMap;
import java.util.Random;

import com.Microservices.GeometryHandler.domain.model.DoubleInt;
import com.Microservices.GeometryHandler.domain.model.IntInt;

public class Tin {
    private static double smallDet = 1.0e-13;

            /// <summary>
        /// Zufallswert für Suche
        /// </summary>
        private Random random = new Random();

        /// <summary>
        /// Punkte des Tin, Index 0 = x, Index 1 = y, Index 2 = z (bzw. Data)
        /// </summary>
        public double[][] Points;

        /// <summary>
        /// Dreiecke des Tin, drei aufeinanderfolgende Indizes definieren ein Dreieck (jeweils der Null basierte Index zum Punkt in Points)
        /// </summary>
        public int[] Triangles;

        /// <summary>
        /// Gegenüberliegende Eckpunkte im Nachbardreieck, Indizes in Triangles, -1 bedeutet kein Nachbardreieck -> Rand
        /// </summary>
        public int[] Opposites;

        private Tin(double[][] points, int[] triangles, int[] opposites)
        {
            Points = points;
            Triangles = triangles;
            Opposites = opposites;
        }

        /// <summary>
        /// Erzeugt Tin mit Nachbarschaft aus einfachem Tin
        /// </summary>
        public static Tin CreateTin(double[][] points, int[] inTriangles, int startIndex)
        {
            int[] triangles = new int[inTriangles.length];
            int[] opposites = new int[triangles.length];
            // Opposites mit -1 initialisieren
            for (int i = 0; i < opposites.length; i+=3)
            {
                int a = inTriangles[i] - startIndex;
                int b = inTriangles[i + 1] - startIndex;
                int c = inTriangles[i + 2] - startIndex;

                // Check ob Dreiecke richtig definiert, sonst Richtungsänderung
                if(determinant(points[a], points[b], points[c]) < 0)
                {                    
                    // (b, c) = (c, b);
                    int d = b;
                    b = c;
                    c = d;
                }


                triangles[i] = a;
                triangles[i+1] = b;
                triangles[i+2] = c;


                opposites[i] = opposites[i+1] = opposites[i+2] = -1;
            }

            // Dictionary mit Kanten (in Java Map), eindeutiger Schlüssel aus Indizes der Eckpunkte
            HashMap<Long, Integer> edges = new HashMap<Long, Integer>();

            // Kanten der Dreiecke durchlaufen, und identische suchen
            for (int i = 0; i < triangles.length; i += 3)
            {
                // Kante ab, gegenüber c
                long ba = ((long)triangles[i + 1] << 32) | (long)triangles[i];
                if (edges.containsKey(ba))
                {
                    int o = edges.get(ba);
                    opposites[i + 2] = o;
                    opposites[o] = i + 2;
                    edges.remove(ba);
                }
                else
                {
                    edges.put(((long)triangles[i] << 32) | (long)triangles[i + 1], i + 2);
                }

                // Kante bc, gegenüber a
                long cb = ((long)triangles[i + 2] << 32) | (long)triangles[i + 1];
                if (edges.containsKey(cb))
                {
                    int o = edges.get(cb);
                    opposites[i] = o;
                    opposites[o] = i;
                    edges.remove(cb);
                }
                else
                {
                    edges.put(((long)triangles[i + 1] << 32) | (long)triangles[i + 2], i);
                }

                // Kante ca, gegenüber b
                long ac = ((long)triangles[i] << 32) | (long)triangles[i + 2];
                if (edges.containsKey(ac))
                {
                    int o = edges.get(ac);
                    opposites[i + 1] = o;
                    opposites[o] = i + 1;
                    edges.remove(cb);
                }
                else
                {
                    edges.put(((long)triangles[i + 2] << 32) | (long)triangles[i], i + 1);
                }
            }

            return new Tin(points, triangles, opposites);
        }

        /// <summary>
        /// Sucht Punkt im Dreiecksnetz, und gibt interpolierten Wert und Index eines Eckpunktes des Dreiecks in dem der Punkt liegt zurück, wenn es geht immer die andere Methode nutzen 
        /// </summary>
        /// <param name="p">Koordinate, index 0 = x, Index 1 = y</param>
        public DoubleInt GetTriangle(double[] p)
        {
            return GetTriangle(random.nextInt(Triangles.length), p);
        }


        /// <summary>
        /// Sucht Punkt im Dreiecksnetz, und gibt interpolierten Wert und Index eines Eckpunktes des Dreiecks in dem der Punkt liegt zurück 
        /// </summary>
        /// <param name="cur">Startindex zum Suchen (aus Rückgabewert der vorherigen Suche)</param>
        /// <param name="p">Koordinate, index 0 = x, Index 1 = y</param>
        public DoubleInt GetTriangle(int cur, double[] p)
        {
            DoubleInt result = new DoubleInt(Double.NaN, 0); 
            int cnt = Triangles.length;

            // In Schleife suchen, bis Dreieck gefunden
            while (cnt-- > 0)
            {
                // System.out.println(cur);
                // andere Dreieckspunkte holen
                IntInt np = getNextPrev(cur);
                int next = np.Value1;
                int prev = np.Value2;

                // Determinante berechnen
                double currDet = determinant(p, next, prev);

                // bei negativer Determinante ins Nachbardreieck
                if(currDet < -smallDet)
                {
                    cur = Opposites[cur];
                    if (cur < 0)
                        cur = Opposites[next] < 0 ? Opposites[prev] : Opposites[next];
                    continue;
                }

                double nextDet = determinant(p, prev, cur);
                double prevDet = determinant(p, cur, next);

                // Abfrage evtl. mit switch optimieren (keine  Ahnung wie Java das macht)
                // Punkt liegt auf Eckpunkt (smallDet soll 0 sein, evtl. Wert optimieren, aber nötig bei double Werten)
                if(Math.abs(currDet) <= smallDet && Math.abs(nextDet) <= smallDet)
                {
                    return new DoubleInt(Points[Triangles[prev]][2], prev);
                }
                // Punkt liegt auf Eckpunkt
                if (Math.abs(currDet) <= smallDet && Math.abs(prevDet) <= smallDet)
                {
                    return new DoubleInt(Points[Triangles[next]][2], next);
                }
                // Punkt liegt auf Eckpunkt
                if (Math.abs(nextDet) <= smallDet && Math.abs(prevDet) <= smallDet)
                {
                    return new DoubleInt(Points[Triangles[cur]][2], cur);
                }
                // weitersuchen (uneindeutig)
                if(nextDet < -smallDet || prevDet < -smallDet)
                {
                    int cont = nextDet < -smallDet && prevDet < -smallDet 
                        ? random.nextBoolean() == true && Opposites[next] >= 0 ? next : prev 
                        : nextDet < -smallDet ? next : prev;
                    
                    cur = Opposites[cont];
                    
                    // Punkt liegt ausserhalb
                    if (cur < 0)
                    {
                        return null;
                    }
                    continue;
                }
                // Punkt liegt auf Rand oder innerhalb
                double sum = currDet + nextDet + prevDet;
                currDet /= sum;
                nextDet /= sum;
                prevDet /= sum;

                // System.out.println(currDet + " " + cur + " " + nextDet + " " + next + " " + prevDet + " " + prev);
                result = new DoubleInt(
                    (currDet * Points[Triangles[cur]][2]) + 
                    (nextDet * Points[Triangles[next]][2]) + 
                    (prevDet * Points[Triangles[prev]][2]), 
                    cur);

            }

        return result;      
        }

        private static IntInt getNextPrev(int cur)
        {
            // int c0 = Math.DivRem(cur, 3, out int ci) * 3;
            int c0 = Math.floorDiv(cur, 3) * 3;
            int ci = Math.floorMod(cur, 3);
            
            return new IntInt(c0 + ((ci + 1) % 3), c0 + ((ci + 2) % 3));
        }

        private static double determinant(double[] a, double[] b, double[] c)
        {
            double dax = a[0] - c[0];
            double day = a[1] - c[1];
            double dbx = b[0] - c[0];
            double dby = b[1] - c[1];

            return (dax * dby) - (day * dbx);
        }

        private double determinant(double[] p, int a, int b)
        {
            return determinant(Points[Triangles[a]], Points[Triangles[b]], p);
        }

        public boolean IsValid()
        {
            if (Points.length < 3
               || Triangles.length < 3
               || Triangles.length % 3 > 0
               || Triangles.length != Opposites.length)
            {
                return false;
            }

            for (int i = 0; i < Opposites.length; i++)
            {
                if ((Opposites[i] >= 0 && Opposites[Opposites[i]] != i)
                    || Triangles[i] < 0
                    || Triangles[i] > Points.length - 1)
                {
                    return false;
                }
            }

            for (int t0 = 0; t0 < Triangles.length; t0 += 3)
            {
                int v0 = Triangles[t0];
                int v1 = Triangles[t0 + 1];
                int v2 = Triangles[t0 + 2];

                int ot = Opposites[t0];
                if (ot >= 0)
                {
                    IntInt np = getNextPrev(ot);
                    if (Triangles[np.Value1] != v2 || Triangles[np.Value2] != v1)
                    {
                        return false;
                    }
                }

                ot = Opposites[t0 + 1];
                if (ot >= 0)
                {
                    IntInt np = getNextPrev(ot);
                    if (Triangles[np.Value1] != v0 || Triangles[np.Value2] != v2)
                    {
                        return false;
                    }
                }

                ot = Opposites[t0 + 2];
                if (ot >= 0)
                {
                    IntInt np = getNextPrev(ot);
                    if (Triangles[np.Value1] != v1 || Triangles[np.Value2] != v0)
                    {
                        return false;
                    }
                }
            }

            return true;
        }


    }

