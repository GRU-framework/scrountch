package scrountch ;

/**
 * Idée initiale est de fournir un accès à des objets graphiques simples depuis Groovy.
 * Chaque objet graphique évolue dans une "cellule" qui fixe les coordonnées d'origine.
 * Dans une fenêtre on peut créer un tableau de cellules.
 * Soit une seule cellule, soit un vecteur, soit un tableau à deux dimensions.
 * Dans le contexte d'une cellule on peut: effacer,
 * dessiner des figures.
 * Tous les objets utilisés (cellules, figures) sont cloneable.
 * Toutes leurs méthodes peuvent avoir des noms internationalisable.
 * C'est facile pour les noms de méthodes, moins pour les noms de classes (voir ClassNode dans AST).
 * On reste dans du awt simple
 */