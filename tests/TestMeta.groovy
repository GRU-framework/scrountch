/**
 *
 * @author bamade
 */
// Date: 11/04/2016

class Smurf {
    int val ;
    public Smurf(int v) {
        this.val = v
    }

    public void arg0() {
       println "arg0"
    }

    public void arg1(Object o, Object o2) {
        println "arg1 $o $o2"
    }
    public void arg1(Object o) {
        println "arg1 $o"
    }

    public void arg2 (Object a, Object b) {
        println "arg2 $a $b"
    }
}

/*
Closure clos0= Smurf.metaClass.
println clos0
Smurf instance = new Smurf(33)
instance.clos0()

//Smurf.metaClass.registerInstanceMethod("doArg0" , clos0)
//Smurf.metaClass.registerInstanceMethod("doArg0" , clos0)
//Smurf.metaClass."doArg0" = Smurf.metaClass."arg0"
Closure clo2 = new MethodClosure(Smurf.metaClass,"arg0")
println clo2
Smurf.metaClass."doArg0" = clo2
/*
println Smurf.metaClass.methods
*/
//Smurf.metaclass."doArg0" = Smurf.metaclass."arg0"

def listM = Smurf.metaClass.metaMethods


println listM

/*
String className = "java.awt.Rectangle"
Class  clazz = Class.forName(className)
String newMethName = "hauteur"
String originalMethName = "getHeight"
String simplified = "height"
println clazz.metaClass
Closure clos =  clazz.metaClass."$simplified".&getGetter
clazz.metaClass.registerInstanceMethod(newMethName, clos)
//clazz.metaClass."$newMethName" = { if (it == null) delegate."$originalMethName" () else delegate."$originalMethName"(*it)}
//clazz.metaClass."$newMethName" = clazz.metaClass."height"
//clazz.metaClass."$newMethName" = { it== null? delegate."$originalMethName"():delegate."$originalMethName"(*it)}
//clazz.metaClass."then$newMethName" = {  delegate.&"$originalMethName".&call }
//println clazz.metaClass."$newMethName".property.class
//println clazz.metaClass."it$newMethName".property.class
 //clazz.metaClass.properties.each {x ->  println x.getName()}
//clazz.metaClass.initialize()
Rectangle rect = new Rectangle(3,4, 30, 400)
//println rect.ithauteur()
//println rect.thenhauteur()
println rect.hauteur()
*/
