package scrountch.geom

import scrountch.GlobalCommons

import javax.swing.*
import javax.swing.plaf.LayerUI
import java.awt.*
/**
 * A SFrame is an extension of <TT>java.awt.Frame</TT> with built-in features to simplify programming
 * in Groovy for beginners:
 * <UL>
 *     <LI> At the center of the frame there are <TT>Cell</TT> instances which are special <TT>Canvas</TT>
 *     for graphics. The cells are displayed with a GridLayout. There may be just one cell,
 *     or a one or two dimensional array of cells.
 *     <LI> To the North and South of the frame there is a <TT>Panel</TT> to display additional Buttons of Labels
 *     <LI> The frame is not resizable, and is disposed when closed. It is  made visible as soon as created.
 *
 * </UL>
 * This class supports the internationalisation of method names
 * @author bear amade
 */
// Date: 23/03/2016

class SFrame extends JFrame {
    //class SFrame extends Frame {
    //class SFrame extends Frame implements I18NMethodNaming{
    final Cell[][] allCells ;
    Panel north;
    Panel south ;

    /**
     * creates a SFrame with just one Cell for graphics
     * @param header title of the frame
     * @param cellWidth width of cell (in points)
     * @param cellHeight height of cell
     */
    public SFrame(String header, int cellWidth, int cellHeight) {
        super(header)
        Cell cell0 = new Cell(cellWidth, cellHeight,0,0)
        allCells = [[cell0]]
        commonInits(1,1)
    }

    /**
     * Creates a SFrame with no title and jsut one Cell
     * @param cellWidth width of cell
     * @param cellHeight height of cell
     */
    public SFrame(int cellWidth, int cellHeight) {
        this("", cellWidth, cellHeight)
    }

    /**
     * creates a SFrame with no title with a single dimensional array of cells (displayed along x dimension)
     * @param cellWidth width of cell
     * @param cellHeight height of cell
     * @param cols number of columns
     */
    public SFrame(int cellWidth, int cellHeight, int cols) {
        this("",cellWidth, cellHeight, 1, cols)
    }

    /**
     * creates a SFrame with a title with a single dimensional array of cells (displayed along x dimension)
     * @param header title of Frame
     * @param cellWidth width of cell
     * @param cellHeight height of cell
     * @param cols number of columns
     */
    public SFrame(String header, int cellWidth, int cellHeight, int cols) {
        this(header,cellWidth, cellHeight, 1, cols)
    }

    /**
     * creates a SFrame with a title with a two dimensional array of cells
     * @param header title of Frame
     * @param cellWidth width of cell
     * @param cellHeight height of cell
     * @param lines number of lines
     * @param cols number of columns
     */
    public SFrame(String header, int cellWidth, int cellHeight, int lines, int cols) {
        super(header)
        allCells = new Cell[lines] [cols]
        for(int iy = 0 ; iy < lines; iy++){
            for(int ix = 0 ; ix < cols; ix++){
                Cell current = new Cell(cellWidth,cellHeight,iy,ix)
                allCells[iy][ix] = current
            }
        }
        commonInits(lines, cols)
    }

    /**
     * creates a SFrame without a title with a two dimensional array of cells
     * @param cellWidth width of cell
     * @param cellHeight height of cell
     * @param lines number of lines
     * @param cols number of columns
     */
    public SFrame(int cellWidth, int cellHeight, int lines, int cols) {
        this("", cellWidth, cellHeight, lines, cols)
    }

    /**
     * private method invoked by constructors
     * @param lines
     * @param cols
     */
    private void commonInits(int lines, int cols) {
        if(lines <0 || cols <0) {
            throw new IllegalArgumentException("Illegal lines: $lines; cols: $cols")
        }
        this.setIconImage(GlobalCommons.scrountchIcon_small.image)
        this.background = Color.LIGHT_GRAY
        LayoutManager manager = new GridLayout(lines, cols, 3,3)
        Panel centre = new Panel(manager)
        centre.background = Color.GRAY
        for(int iy = 0 ; iy < lines; iy++){
            for(int ix = 0 ; ix < cols; ix++){
                Cell cell = allCells[iy][ix]
                /*
                /**/
                //centre.add(new LayeredPane(cell))
                //centre.add(cell)
                /**/
                //JPanel panel = new LayeredPane(cell)
                LayerUI<JComponent> layerUI = new GlassOverCell(cell)
                JLayer<Component> jlayer = new JLayer<Component>(cell, layerUI);
                centre.add(jlayer)
                /**/
            }
        }
        this.add(centre)
        north = new Panel()
        north.background = Color.LIGHT_GRAY
        south = new Panel()
        south.background = Color.LIGHT_GRAY
        this.add(north, BorderLayout.BEFORE_FIRST_LINE)
        this.add(south, BorderLayout.AFTER_LAST_LINE)
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)

        /*
        this.addWindowListener(new WindowAdapter(){
            @Override
            void windowClosing(WindowEvent e) {
                dispose()
            }
        }) ;
        */
        this.setResizable(false)
        this.pack()
        this.setVisible (true)
        for(int iy = 0 ; iy < lines; iy++) {
            for (int ix = 0; ix < cols; ix++) {
                allCells[iy][ix].postInit()
            }
        }
    }

    /**
     *  gets the cell at given coordinates in an array of cells.
     *  All coordinates start at zero
     * @param coordy
     * @param coordx
     * @return the cell at given coordinates
     */
    public Cell getCell(int coordy, int coordx) {
        return allCells[coordy] [coordx]
    }

    /**
     * @param coordx the X coordinate of the required Cell (starts at zero)
     * @return the cell at the first line of cells
     */
    public Cell getCell(int coordx) {
        return getCell(0,coordx)
    }

    /**
     * @return   the background cell (if there is only one Cell in the SFrame)
     */
    public Cell getCell( ) {
        return getCell(0,0)
    }

    /**
     *
     * @return a two dimensional array of cells contained in this SFrame.
     *     <B>WARNING</B>: do not modify this array!
     */
    public Cell[][] cells() {
        return allCells
    }

    /**
     * adds on all Cells of this SFrame a specific <TT>MouseListener</TT> that
     * reacts to MouseClick
     * @param clos method closure with specs described in method below
     * @see scrountch.geom.Cell#onClick(Closure)
     */
    public void allOnClick(Closure clos) {
        for(int ix = 0 ; ix < allCells.length; ix++) {
            for(int iy= 0 ; iy < allCells[ix].length; iy++) {
                Cell cell = allCells[ix] [iy]
                cell.onClick(clos)
            }
        }
    }

    /**
     * adds a component to the South Panel
     * @param comp
     */
    public void addSouth(Component comp) {
        south.add(comp)
        validate()
    }

    /**
     * removes the component from the South Panel
     * @param comp
     */
    public void removeSouth(Component comp) {
        south.remove(comp)
        validate()
    }

    /**
     * adds a Component to the North Panel
     * @param comp
     */
    public void addNorth(Component comp) {
        north.add(comp)
        validate()
    }

    /**
     * removes a Component from the North Panel
     * @param comp
     */
    public void removeNorth(Component comp) {
        north.remove(comp)
        validate()
    }


}
