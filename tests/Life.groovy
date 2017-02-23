

/**
 *
 * @author bamade
 */
// Date: 25/03/2016

class Life {
    int[][] model ;
    int width;
    int height;


    public void generation() {
        int[][] doppel = new int[height][width]
        for(int iy=0; iy < height; iy++ ) {
            for(int ix=0; ix < width; ix++) {
                int value = model[iy][ix]
                   int previousX = indexReel(ix-1, width)
                   int nextX = indexReel(ix+1, width)
                   int previousY = indexReel(iy-1, height)
                   int nextY = indexReel(iy+1, height)
                   int neighbors = model[iy][previousX]+
                   model[previousY][previousX]+
                   model[nextY][previousX]+
                   model[previousY][ix]+
                   model[nextY][ix]+
                   model[iy][nextX]+
                   model[previousY][nextX]+
                   model[nextY][nextX] ;
            if (value ==0 ){
                if(neighbors == 3) {
                    doppel[iy][ix] =1
                }

            } else {
                if(neighbors == 2 || neighbors == 3) {
                    doppel[iy][ix] =1

                }
            }
            }
        }
        /*
        for(int iy=0; iy < height; iy++ ) {
            println "---> "+ doppel[iy]
        }
        */

        model = doppel
    }

    public int indexReel(int index, int taille) {
        int idx =index %taille
        if(idx <0 ) {
            idx = idx +taille
        }
        return idx
    }

    public static void main(String[] args) {
        Life life = new Life()
        life.model = [
                [0,0,0,0,0,0,0,0,1],
                [0,0,0,0,0,0,0,0,1],
                [0,0,0,0,0,0,0,0,1],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0],
                [0,0,0,0,0,1,1,0,0],
                [0,0,0,0,0,1,1,0,0],
                [0,1,0,0,0,0,0,0,0],
                [0,1,0,0,0,0,0,0,0],
                [0,1,0,0,0,0,0,0,0],
                [0,0,0,0,0,0,0,0,0]
        ] ;
        life.width = life.model[0].length
        life.height = life.model.length

        for(int ai=0 ; ai < 5; ai++){
            life.generation()
            for(int a; a < life.model.length; a++)
                println(life.model[a])
            println "----------------"
        }

    }
}
