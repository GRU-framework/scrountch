package scrountch ;

/**
 * Id�e initiale est de fournir un acc�s � des objets graphiques simples depuis Groovy.
 * Chaque objet graphique �volue dans une "cellule" qui fixe les coordonn�es d'origine.
 * Dans une fen�tre on peut cr�er un tableau de cellules.
 * Soit une seule cellule, soit un vecteur, soit un tableau � deux dimensions.
 * Dans le contexte d'une cellule on peut: effacer,
 * dessiner des figures.
 * Tous les objets utilis�s (cellules, figures) sont cloneable.
 * Toutes leurs m�thodes peuvent avoir des noms internationalisable.
 * C'est facile pour les noms de m�thodes, moins pour les noms de classes (voir ClassNode dans AST).
 * On reste dans du awt simple
 */