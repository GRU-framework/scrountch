package scrountch.utils

import java.lang.ref.ReferenceQueue

/*
 *

 */
import java.lang.ref.SoftReference

/**
 * A data cache mostly to be used with Images.
 * Implementation: beware to many synchronized access !
 */

public class Cache<K, X> {


    private LinkedHashMap<K, X> strongMap;
    private HashMap<K, SoftEntry> softMap;
    private ReferenceQueue<X> queue = new ReferenceQueue<X>();

    class MyLinkedMap extends LinkedHashMap<K, X> {
        private int sizeMax;

        public MyLinkedMap(int baseSize, int maxSize) {
            super(baseSize, 0.75F, true);
            sizeMax = maxSize;
        }

        protected boolean removeEldestEntry(Map.Entry<K, X> eldest) {
            if (size() > sizeMax) {
                X val = eldest.getValue();
                K clef = eldest.getKey();
                // les valeurs nulles n'entrent pas dans deuxi�me map
                if (val != null) {
                    // acc�s synchronis� par appel du put
                    softMap.put(clef, new SoftEntry(clef, val, queue));
                }
                return true;
            }
            return false;
        }

    }

    public class SoftEntry<K, X> extends SoftReference<X> {
        K key;

        SoftEntry(K k, X value, ReferenceQueue<X> queue) {
            super(value, queue);
            this.key = k;
        }

        public boolean enqueue() {
            // appel� par thread du Garbage!
            synchronized (Cache.this) {
                softMap.remove(key);
            }
            // pr�venir le Listener �ventuel
            return true; // est ce une bonne id�e?
        }

    }

    public Cache(int baseSize, int maxSize) {
        strongMap = new MyLinkedMap(baseSize, maxSize);
        // choisir une petite taille ?
        softMap = new HashMap<K, SoftEntry<K, X>>();
    }

    // pour ces m�thodes voir les remarques g�n�rales
    // (containskey et valeurs nulles)
    public synchronized boolean containsKey(K clef) {
        return strongMap.containsKey(clef) || softMap.containsKey(clef);
    }

    /** Ici put renvoie void
     * */
    public synchronized void put(K clef, X arg) {
        softMap.remove(clef);
        strongMap.put(clef, arg);
    }

    /** si renvoie null:
     *    valeur contenue Est nulle
     *    valeur non connue
     *    valeur connue mais r�cup�ree par garbage collector (peu de chance)
     */
    public synchronized X get(K clef) {
         X res  = strongMap.get(clef);
        if(res != null) {
            return res
        }
        SoftEntry<K, X> entry = softMap.get(clef);
        if(entry != null) {
            return entry.get();
        }
        return null ;
    }


}
