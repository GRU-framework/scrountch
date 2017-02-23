

import scrountch.geom.*

import java.awt.*

/**
 *
 * @author bamade
 */
// Date: 23/03/2016

class TestB {
    static IconImage icon = new IconImage("draught1.jpg")
    static GraphicImage grImage = new GraphicImage(icon, 20)
    public static void main(String[] args) {
        SFrame cadre = new SFrame(50,50, 10, 10)
        cadre.setTitle("Jeu de la Vie")
        SButton dem = new SButton("start", "démarrer")
        dem.onClick (TestB.&act)
        dem.addActionListener(TestB.&hello)
        cadre.addSouth(dem)
        cadre.addSouth(new Button("arréter"))
        //IconImage icon = new IconImage("draught1.jpg")
        //GraphicImage grImage = new GraphicImage(icon, 20)
        //Cell cell = cadre.getCell(2,2)
        Cell cell0 = cadre.getCell(0,0)
        cadre.allOnClick  (TestB.&addImage)
        Cell cell = cadre.getCell(2,2)
        Cell cell2 = cadre.getCell(3,3)
        Cell cell3 = cadre.getCell(4,4)
        //cell.preUpdate = {g -> println " PRE  "}
        //cell.postUpdate = {g -> println " POST : $g"}
        for(int ix = 0 ; ix < 20; ix++) {
            cell.addToGraphics(grImage)
            cell3.addToGraphics(grImage)
            cell2.clear()
            Thread.sleep(1000)
            cell2.addToGraphics(grImage)
            cell.clear()
            cell3.clear()
            Thread.sleep(1000)
        }

    }

    public static void addImage(Cell goal) {
        goal.addToGraphics(grImage)
    }
    public static void act(String name) {
        println name
    }
    public static void hello( evt)  {
        println "HELLO"
    }

}
