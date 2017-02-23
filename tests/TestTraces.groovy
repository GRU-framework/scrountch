import scrountch.Fab

/**
 *
 * @author bamade
 */
// Date: 27/05/2016


for (int ix =  0; ix < 10 ; ix++) {
    if(ix%2 == 0) {
        Fab.stopTraces()
    } else {
        Fab.enableTraces("test")
    }
    Fab.trace(" trace $ix", "test")
}
 
