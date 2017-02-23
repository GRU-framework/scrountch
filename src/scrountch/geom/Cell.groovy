package scrountch.geom

import scrountch.GlobalCommons
import scrountch.utils.SelectionListener

import java.awt.*
import java.awt.event.*
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.awt.image.BufferStrategy
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.logging.Level

/**
 *  a <code>Canvas</code> with special features.
 *  It contains a list of <TT>GraphicObject</TT> that are drawn each time an instance
 *  of this class is repaint.
 *  <BR>
 *   Implementation note: there is a circular dependency of this class with
 *   <TT>GraphicObject</TT> and its subclasses. Beware when modifying.
 * @author bear amade
 */
// Date: 22/03/2016

class Cell extends Canvas {
    //class Cell extends JPanel  { does not work

    /**
     * Transparent Color: to be shared
     */
    static final Color TRANSPARENT = new Color(0, 0, 0, 0)
    // BORDER COLLISION CONSTANTS
    // Check
    static final int NORTH = 1
    static final int EAST = 1 << 3
    static final int NORTH_EAST = NORTH + EAST
    static final int SOUTH = 1 << 5
    static final int SOUTH_EAST = SOUTH + EAST
    static final int WEST = 1 << 7
    static final int SOUTH_WEST = SOUTH + WEST
    static final int NORTH_WEST = NORTH + WEST

    /**
     * Borders will be used for detection of collisions with shapes.
     * They are rectangles with a 0.1D width
     * TODO: make these GraphicShapes not in objectList (or a special class GraphicZone)
     */
    final GraphicZone upperBorder
    final GraphicZone lowerBorder
    final GraphicZone leftBorder
    final GraphicZone rightBorder
    /**
     * the list of contained GraphicObjects
     */
    final LinkedHashSet<GraphicObject> objectList = new LinkedHashSet<GraphicObject>()

    /**
     * implementation note: later to be changed in an array of layers
     * (one for each GraphicComponent)
     */
    GlassOverCell layer0

    /**
     *  for black belt programmers: code to be executed before each update.
     *  once set the usual clearing of the Graphics context do not happen!
     *  The Graphics2D context should be an argument of the Closure
     */
    Closure preUpdate;

    /**
     *  for black belt programmers: code to be executed after each update.
     *  The Graphics2D context should be an argument of the Closure
     */
    Closure postUpdate;

    /**
     * identifies the current Cell with y position in containing Frame
     */
    final int linePos;

    /**
     * identifies the current Cell with x position in containing Frame
     */
    final int colPos;

    /**
     * do not tweak unless you are a black belt
     */
    BufferStrategy strategyForBuffer;

    java.util.List<CollisionSpec> collisionSpecs = []

    protected Closure collisionAction

    /**
     * each cell maintains a Map of Observers for Image handling.
     * See ImgObserver doc.
     */
    Map<Object, ImgObserver> mapObservers = [:]

    static final AffineTransform IDENTITY = new AffineTransform()
    static final DEFAULT_COLOR = Color.LIGHT_GRAY.brighter()

    /**
     * This constructor should not be invoked by user code: it is invoked only by the
     * <TT>SFrame</TT> that creates the cells.
     * @param width
     * @param height
     * @param line the line coordinate at which the cell has been created
     * @param col the col coordinate at which the cell has been created
     */
    public Cell(int width, int height, int line, int col) {
        this.setBackground(DEFAULT_COLOR)
        this.setSize(width, height)
        //this.setIgnoreRepaint(true)
        Dimension dimension = new Dimension(width, height)
        this.setMinimumSize(dimension)
        this.setPreferredSize(dimension)
        linePos = line
        colPos = col
        upperBorder = new GraphicZone(new Area(new Rectangle2D.Double(0, 0, width, 0.1D)))
        lowerBorder = new GraphicZone(new Area(new Rectangle2D.Double(0, height, width, 0.1D)))
        leftBorder = new GraphicZone(new Area(new Rectangle2D.Double(0, 0, 0.1D, height)))
        rightBorder = new GraphicZone(new Area(new Rectangle2D.Double(width, 0, 0.1D, height)))
    }

    /**
     * method to be invoked after the instance has been created and the corresponding
     * peer is visible on the screen (mostly handles BufferStrategy)
     */
    void postInit() {
        this.createBufferStrategy(2)
        strategyForBuffer = this.getBufferStrategy()
    }

    /**
     *
     * @return the Graphics2D context of this canvas (use with care)
     */
    public Graphics2D getGraphics() {
        if (strategyForBuffer != null) {
            return strategyForBuffer.getDrawGraphics() as Graphics2D
        }
        return super.getGraphics() as Graphics2D
    }

    /**
     * internal method (used by ImgObserver)
     * @param gr
     */
    public void dump(Graphics2D gr) {
        if (strategyForBuffer != null) {
            gr.dispose()
        }
    }

    /**
     * @return a copy of the list of GraphicObjects managed by this canvas
     */
    public java.util.List<GraphicObject> getList() {
        java.util.List<GraphicObject> res = []
        for (GraphicObject obj : objectList) {
            res.add(obj)
        }
        return res
    }

    /**
     * adds all the GraphicObjects to the List of GraphicObjects managed by this canvas.
     * This method repaints the canvas
     * @param list list of GraphicObjects to addNoRepaint to the management
     */
    public void addAll(java.util.List<GraphicObject> list) {
        for (GraphicObject obj : list) {
            objectList.add(obj)
        }
        forceRepaint()
    }

    /**
     * adds a Graphic Object to the list of GraphicObjects managed by this canvas.
     * This method repaints the canvas
     * @param obj a GraphicObject to addNoRepaint
     * @return the current Cell
     */
    public Cell addToGraphics(GraphicObject obj) {
        objectList.add(obj)
        forceRepaint()
        return this
    }

    /**
     * adds a Graphic Object to the list of GraphicObjects managed by this canvas.
     * This method does not repaint the canvas
     * @param obj
     * @return the current Cell
     */
    public Cell addNoRepaint(GraphicObject obj) {
        objectList.add(obj)
        return this
    }

    /**
     * removes a given GraphicObject from the list managed by this canvas.
     * This method repaints the canvas
     * @param obj
     * @return the current Cell
     */
    public Cell removeFromGraphics(GraphicObject obj) {
        objectList.remove(obj)
        mapObservers.remove(obj)
        //TODO: remove from CollisionSearch
        //todo: change : create flickers
        forceRepaint()
        return this
    }

    /**
     * removes a Graphic Object from the list of GraphicObjects managed by this canvas.
     * This method does not repaint the canvas
     * @param obj
     * @return the current Cell
     */
    public Cell remove(GraphicObject obj) {
        objectList.remove(obj)
        mapObservers.remove(obj)
        return this
    }
    /**
     * remove all GraphicObjects managed by this canvas
     * Does not repaint the current Cell
     * @return the current Cell
     */
    public Cell clear() {
        objectList.clear()
        mapObservers.clear()
        return this
    }

    /**
     * remove all GraphicObjects managed by this canvas
     * repaints the current Cell
     * @return the current Cell
     */
    public Cell clearAllGraphics() {
        objectList.clear()
        //todo: mapObservers
        mapObservers.clear()
        forceRepaint()
        return this
    }

    /**
     * registers a <TT>MouseListener</TT> to the current canvas.
     * This mouse listener handles only simple clicks.
     * It invokes the closure on click.
     * This closure may have:
     * <UL>
     *     <LI> no parameter
     *     <LI> a single <TT>Cell</TT>parameter which is the Cell which has been "clicked"
     *     <LI> 3 parameters: the Cell, the x coordinate of the click and the y coordinate of the click
     *     </UL>
     * @param clos a Closure with abovementioned specifications
     * @return a MouseListener
     */
    public MouseListener onClick(Closure clos) {
        Class[] parmsTypes = clos.getParameterTypes()
        int size = parmsTypes.length
        switch (size) {
            case 0: break;
            case 3: // todo verify parms1 and 3 bein ints
            case 1: if (!(Cell.class.equals(parmsTypes[0]))) {
                throw new IllegalArgumentException("onClick arg should be a Closure with Cell as first arg")
            }
                break;
            default:
                throw new IllegalArgumentException("onClick arg should be a Closure with proper args")
        }
        MouseListener res = new MouseAdapter() {
            @Override
            void mouseClicked(MouseEvent e) {
                switch (size) {
                    case 0: clos(); break;
                    case 1: clos(Cell.this); break;
                    case 3: clos(Cell.this, e.x, e.y); break;

                }
            }
        }
        this.addMouseListener(res)
        return res
    }

    /**
     * registers a procedure that will take a <TT>Cell</TT> parameter that will be invoked
     * when a mouse is clicked on one Cell.
     * @param consumer
     * @return
     */
    public MouseListener onClick(Consumer<Cell> consumer) {
        return onClick({Cell c -> consumer.accept(c)})
    }

    /**
     * registers a procedure that takes a <TT>Cell</TT> and a <TT>Point2D</TT> parameter
     * that will be invoked when a mouse is clicked on one Cell, the point parameter gets
     * the coordinates of the click on the current Cell.
     * @param biconsumer
     * @return
     */
    public MouseListener onClick(BiConsumer<Cell, Point2D> biconsumer) {
        return onClick( {Cell cell, double x, double y -> biconsumer.accept(cell, new Point2D.Double(x,y))})
    }

    /**
     * registers a no-arg procedure that will be invoked when the current Cell is clicked.
     * @param runnable
     * @return
     */
    public MouseListener onClick(Runnable runnable) {
        return onClick({runnable.run()})
    }

    /**
     * registers a CLosure that takes a <TT>Rectangle2D</TT> argument that will be invoked
     * when a Mouse will be dragged to select a rectangle in the current Cell.
     * @param selectionAction
     * @return A composite Listener of type <TT>SelectionListener</TT> (that can be removed by method
     * <TT>removeSelectionListener</TT>)
     */
    public SelectionListener onSelection(Closure selectionAction) {
        Point startDrag = null
        Point endDrag = null
        MouseListener mlist = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startDrag = new Point(e.getX(), e.getY());
            }

            public void mouseReleased(MouseEvent e) {
                if (endDrag != null) {
                    endDrag = new Point(e.getX(), e.getY());
                    Rectangle2D.Double r = new Rectangle2D.Double(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(endDrag.x - startDrag.x), Math.abs(endDrag.y - startDrag.y));
                    startDrag = null;
                    endDrag = null;
                    //todo: is this possible?
                    Graphics2D gr = Cell.this.getGraphics()
                    // the layer should now be completely transparent
                    layer0.showSelection(gr, null)
                    gr.dispose()
                    // apparently does not work without that! strange
                    forceRepaint()
                    //here every painting should be finished! how to be sure?
                    selectionAction(r)
                }
            }
        }
        MouseMotionListener mMotion = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                endDrag = new Point(e.getX(), e.getY());
                Rectangle2D.Double r = new Rectangle2D.Double(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(endDrag.x - startDrag.x), Math.abs(endDrag.y - startDrag.y));
                //todo: is this possible?
                Graphics2D gr = Cell.this.getGraphics()
                layer0.showSelection(gr, r)
                gr.dispose()
                // apparently does not work without that! strange
                forceRepaint()
            }
        }
        SelectionListener compList = new SelectionListener(mlist, mMotion)
        addSelectionListener(compList)
        return compList
    }

    /**
     * registers a procedure that takes a <TT>Rectangle2D</TT> parameter
     * that will be invoked
     * when a Mouse will be dragged to select a rectangle in the current Cell.
     * @param consumer
     * @return
     */
    public SelectionListener onSelection(Consumer<Rectangle2D> consumer){
        return onSelection({Rectangle2D r-> consumer.accept(r)})
    }

    /**
     *
     * @param compoM
     */
    public void addSelectionListener(SelectionListener compoM) {
        this.addMouseListener(compoM.mouseListener)
        this.addMouseMotionListener(compoM.motionListener)
    }

    /**
     * removes a composite SelectionListener from the Mouse events handling on this Cell
     * @param compoM
     */
    public void removeSelectionListener(SelectionListener compoM) {
        this.removeMouseListener(compoM.mouseListener)
        this.removeMouseMotionListener(compoM.motionListener)
    }

    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e)
    }

    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e)
    }

    CollisionSpec detectBordersCollision(GraphicObject grObj) {
        if (!objectList.contains(grObj)) {
            throw new IllegalArgumentException(grObj + " not in Cell")
        }
        if (grObj.initialArea == null) {
            grObj.initArea(this)
        }
        CollisionSpec res = new BordersCollisionSpec(grObj)
        collisionSpecs.add(0, res)
        return res
    }

    CollisionSpec detectObjectsCollision(GraphicObject first, GraphicObject second) {
        if (first.initialArea == null) {
            first.initArea(this)
        }
        if (second.initialArea == null) {
            second.initArea(this)
        }
        CollisionSpec res = new GraphicObjectsCollisionSpec(first, second)
        collisionSpecs.add(res)
        return res
    }

    void removeCollisionDetection(CollisionSpec spec) {
        collisionSpecs.remove(spec)
    }

    void onCollision(Closure action) {
        if (action == null) {
            collisionAction = null
            return
        }
        Class[] argTypes = action.getParameterTypes()
        // check if: int, int, CollisionEvent
        if (!argTypes[2].equals(CollisionEvent.class)) {
            throw new IllegalArgumentException(" collision action should have arg: int, int, CollisionEvent")
        }
        collisionAction = action
    }

    /*
    COLLISION detection specs:
      - partners: Object/Object or Object/border
      - nature of test:
         * simple line (for borders only)
         * bounds
         * Area
      before any update if the CollionSpecs is not empty
      each collision is checked
      if list not empty a specific method is invoked
      returning: nbOfTries + list of Collision detection
      nbOfTries > 1 means there is something stuck
      if nbOfTries > 2 an exception is fired
      Collision object contains: the Two objects
      (or constants NORTH, EAST, NORTH_EAST etc.. for borders)
      + the area of collision (or simpler data?)
      each spec should bear an ID (so the code could identify the collision)

     */

    void collisionDetection() {
        // loop while collisionPresent
        // increment counter
        // for each graphicObject get final affineTransform
        // first go for BordersDetection -> accumulate
        // set int for NORTH, WEST, etc.
        // then objects detection
        // if user's method return true gets out
        // otherwise loops again

    }

    public void paint(Graphics gr) {
        //println ("paint cell")
        update(gr)
        //println ("after paint")
    }

    /**
     * update the graphics by painting all the GraphicObjects managed by this canvas
     * THis method inner code SHOULD be synchronized
     * @param gr
     */
    public void update(Graphics gr) {
        update(gr, true)
    }

    public void update(Graphics gr, boolean possibleBuffer) {
        try {
            //strange test: we do not carry collision detection when dealing with graphics
            // used for Images of the Cell
            if (possibleBuffer && collisionAction != null) {
                collisionDetection()
            }
            //println "before update"
            synchronized (this) {
                Graphics2D graphics2D
                if (possibleBuffer) {
                    if (strategyForBuffer != null) {
                        graphics2D = strategyForBuffer.getDrawGraphics() as Graphics2D
                    } else {
                        graphics2D = gr as Graphics2D
                    }
                } else {
                    graphics2D = gr as Graphics2D
                }
                if (preUpdate != null) {
                    preUpdate(graphics2D)
                } else {
                    graphics2D.clearRect(0, 0, getWidth(), getHeight())
                    //TODO: repaint the background ?

                }
                //seems better!
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Stroke stdStroke = graphics2D.getStroke()
                //graphics2D.setPaint(this.getBackground())
                Paint stdFill = graphics2D.getPaint()
                Color stdDrawPaint = this.foreground
                Color stdBackGround = this.background
                Shape stdClip = graphics2D.getClip()
                Font stdFont = this.getFont()
                AffineTransform defaultTransform = graphics2D.getTransform()
                //AffineTransform defaultTransform = IDENTITY
                if (!stdBackGround.equals(DEFAULT_COLOR)) {
                    graphics2D.setColor(stdBackGround)
                    graphics2D.fillRect(0, 0, getWidth(), getHeight())
                    graphics2D.setColor(stdDrawPaint)
                }
                /*
                 TODO: change this loop for Collision detection
                 for each GraphicObject first perform the "move" method
                 (to be written: should contain all moves and transformations)
                  then detect collision (and operate modified moves again)
                  then draw
                  see specs in Collisions class
                 */
                for (GraphicObject grObject : objectList) {
                    boolean transformed = false;
                    try {
                        //AffineTransform trCompose = grObject.getTransformationBeforeRotation()
                        //println("before draw")
                        Shape clip = grObject.clip
                        if(clip != null) {
                            if(stdClip != null) {
                               // union of shapes
                                // todo: test and check if not Area
                                clip = new Area(stdClip)
                                clip = clip.add(new Area(grObject.clip))
                            }
                            graphics2D.setClip(clip)
                        }
                        //do we have to save the transform since the transform is changed by each grObject?
                        grObject.draw(this, graphics2D)
                        //println("after draw")

                    } catch (Exception exc) {
                        GlobalCommons.LOG.log(Level.WARNING, "cell graphics", exc)

                    } finally {
                        graphics2D.stroke = stdStroke
                        graphics2D.paint = stdFill
                        graphics2D.color = stdDrawPaint
                        graphics2D.font = stdFont
                        graphics2D.setTransform(defaultTransform)
                        graphics2D.setClip(stdClip)
                    }
                }
                if (postUpdate != null) {
                    postUpdate(graphics2D)
                }
                layer0.glassPaint(graphics2D)
                if (strategyForBuffer != null) {
                    strategyForBuffer.show()
                    graphics2D.dispose()
                }
            }

            //println "after update"
        } catch (Exception exc) {
            GlobalCommons.LOG.log(Level.FINE, "update (interrupted?)", exc)
        }
    }

/**
 * this method overriden just to force synchronization.
 * The main reason is that imageObserver should not update images
 * while the Cell is repainted.
 */
    public void repaint()
//public synchronized void repaint()
    {
        //println "before repaint"
        super.repaint()
        //println "after repaint"
    }

/**
 * this method is different from the usual repaint().
 * The reason is that repaint events may be erased from the event queue
 * while this specific repaint is mandatory!
 * You can't repaint and forceRepaint at the same time (+ ImgObservers should not collide
 * with repaint events)
 */
    public void forceRepaint() {
        //public synchronized  void  forceRepaint() {
        if (strategyForBuffer != null) {
            update(null)
        } else {
            Graphics gr = getGraphics()
            update(gr)
            //revalidate()
        }
        //repaint()
    }


    boolean borderCollision(Area border, Shape shape) {
        Area collider = new Area(shape)
        collider.intersect(border)
        return !collider.isEmpty()
    }
}
