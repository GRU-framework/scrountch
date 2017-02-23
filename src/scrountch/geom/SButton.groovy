package scrountch.geom

import scrountch.GlobalCommons

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.function.Consumer
import java.util.logging.Level

/**
 * This subclass of Button needs two Strings:
 * <UL>
 *     <LI> the Label to be displayed
 *     <LI> the name of the button (this is needed for the <TT>OnClick</TT> Closure because
 *     it helps to identify which button has been clicked -this is a simplification because
 *     the feature can be programmed using standard ActionEvent-)
 *     </UL>
 * @author bear amade
 */
// Date: 24/03/2016

class SButton extends Button {
    //class SButton extends Button implements I18NMethodNaming {

    /**
     * creates a button with an ID and a Label
     * @param ref the ID of the button
     * @param label string to be displayed
     */
    public SButton(String ref, String label) {
        super(label);
        this.setActionCommand(ref);
    }

    /**
     * @return the ID of the button
     */
    public String getRef() {
        return this.getActionCommand();
    }

    /**
     * registers a function to handle event when the button is clicked.
     * The function can be without a parameter or with a String parameter:
     * the parameter will be then the Id of the clicked Button
     * @param clos
     * @return an ActionListener that has been registered to the button
     */
    public ActionListener onClick(Closure clos) {
        // 3 cases: no arg, 1 arg and 3 args
        ActionListener res = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Class[] parms = clos.getParameterTypes()
                switch (parms.length) {
                    case 0: clos(); break;
                    case 1: clos(getRef()); break;
                    default:
                        GlobalCommons.LOG.log(Level.WARNING, "onclick received a method with incorrect number of parameters")
                        break;
                }
            }
        };
        this.addActionListener(res)
        return res
    }

    /**
     * registers a no-argument procedure do handle a "mouse clicked" event on the SButton
     * @param runnable
     * @return an ActionListener that has been registered to the Button
     */
    public ActionListener onClick(Runnable runnable) {
        return onClick {runnable.run()}
    }

    /**
     * registers a String Consumer to handle a "mouse clicked" event.
     * the argument passed to the Consumer will be the ID of the Button.
     * @param consumer
     * @return
     */
    public ActionListener onClick(Consumer<String> consumer) {
        return onClick({String action -> consumer.accept(action)})
    }

}

