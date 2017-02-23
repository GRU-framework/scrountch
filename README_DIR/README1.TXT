The scrountch  library has been designed as a tool to teach programming.

I (Bear AMADE) wrote a book about  programming for beginners and for the labs it uses a subset of Groovy.
(The book is in french but if an editor in the english-speaking world is interested ... BTW translations
in other languages will delight me!)

The problem with those labs is that they are not very funny ... So I created a library  to
access simplifed graphic features. The other important feature of this library is that class names
and method names can be translated in the user's language. (the keywords are the last english words left)

Please note that the tool IS NOT designed to write very efficient graphical games ... just to learn programming while
playing with shapes and images.

Now the code is in it's first version and there are probably many things that should be changed for future versions:


- I am not a graphical programming specialist: the code is based on java.awt ... may be it should be moved to javafx
or another library with more options (but please, again, the goal of the library is not to write fancy and efficient
games). An example of possible modification is to use layers to display graphic objects in a Cell (each object being
in its own layer)
introducing layers could be like this:
 * each Cell may maintains a list of layers: when updated only the layers tagged as modified will be updated
 * each Graphic Object will maintain a list of layers on which it is present: each time it's state is modified
 then the corresponding layer will be tagged as modified.
 * the modification of the Graphic object is not always obvious if the shape managed by a Graphic Shape is modified
  (for instance it's a Turtle of a Path2D) then the Graphic Shape should be notified!


- There is a known bug about the centered rotation: the "center" of the figure may change during rotation
and this may create strange movements during animation. So this should be changed.
(the fix is not obvious)

- Though we are not very keen on efficient codes some optimisations are still needed.
**  The updates of the Cell are very complex (animated gifs are a nuisance to deal with)
**  the same codes may be executed twice each time you want to repaint a Cell: you may, for instance, execute code
for collision detection and the same code will be executed while repainting ...so something can be done about it.
** internationalisation of method names can be made more efficient

- Some features should be added: sounds, simplified paints, better access to image transformations, more efficient
collision detection

- another additional effort could be made to translate even keywords. Mind you: typing "while" if you are french
is not a problem but I wonder what happens is you have, let's say, a cyrillic keyboard.
Is it something to do? (for instance a simple text filtering of the file with translated keywords beginning with a
special character -for instance #-). Now what to do with text orientation (arabic)? In that later case
does the console allows right-to-left text parsing? (is the same possible when reading from a File?)

A tutorial (in french) is in the "docs_n_tutorials" directory.

Note: the "scrountch" name (pronounce "scroon-tsh") is like "crunchy" in french. For sure I was thinking
about the MIT's "Scratch" tool (though scrountch plays in a fairly different league: it's not Scratch, Snap, or Blockly
the goal is completely different: it slowly introduces programming with "text source" code -with some strong principles-)

bear.amade (where?) -> gmail.com