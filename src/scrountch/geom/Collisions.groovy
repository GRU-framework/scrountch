package scrountch.geom

/**
 * conflicting  specifications for Collision detection.
 * see Cell's code or this doc ... and think!
 * the draw method for GraphicOjects should be split:
 *  <LI>
 *      <UL> the first part of the present method is about moves of the graphical object
 *      this should be run in a separate method (invoked by draw)
 *      <UL> when all "moves" have been run the Graphic Object should wait for
 *      all other GraphicObjects in the same Cell so each perform its "move"
 *      <UL> the moves method should be distinct from the transformations of the Graphics2D (otherwise multiple transformations may be operated!)
 *      <UL> when all moves have been invoked then the Cell code
 *      should operate Collision detection
 *      (if any Handling Closure is registered for any GraphicObject in the Cell).
 *
 *      The collision might be between different GraphicObjects or between GraphicObjects
 *      and GraphicZOnes: this is searched by calculating Area subtract (if the resulting
 *      Area is empty then there is no Collision - performance is not a major issue-).
 *
 *      each GraphicObject may have a Collision research Closure
 *      the code of the Closure may invoke a collisionWith(Object) method that yields
 *      the common Area
 *      it's up to the programmer to operate new moves to avoid collision
 *      before drawing.
 *      This may be difficult because a graphic Object my be colliding with various
 *      other objects and the moves may create other new collisions
 *
 *      <UL> now draw all Graphics
 *
 *      </LI>
 *
 * @author bamade
 */
// Date: 20/02/2017

class Collisions {
}
