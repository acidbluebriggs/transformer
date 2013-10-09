/**
 *
 * <p>
 * The acidblue Reosurce Bundle API uses annotations to define resources
 * and provides said resources with a default value. This aids in
 * development since the developer no longer needs to drop out of writing java
 * code to edit property files and provides automatic namespacing and resource
 * bundle key/value generation.
 * </p>
 *
 *
 * <h3>Defining resources</h3>
 *
 * <p>
 * The default java resource bundle implementation can generally be defined as a simple map of keys and values.
 * The key is simply a human readable, non-language specific, pointer that is used for locating a language
 * specific value.  In most cases these keys and values are defined in a simple text based java property file.
 * In most cases values are simply strings.  If something other than a String is needed (such as an icon, a color,
 * or any other arbitrary java type that can be serialized as a string) a developer must create/use multiple
 * APIs to read and convert that string into the necessary type.  Not to mention, the developer must take
 * great care in defining unique keys and ensure that these keys are present within the resource bundle along
 * with a value.  Else, the developer must deal with a endless null checking and then must provide default
 * values to fall back on.
 *
 * <p>
 * For instance, if a developer were to need to look up an Icon located within a resource bundle, the developer
 * would probably do something like this:
 *
 * <ol>
 *    <li>
 *         <strong>Define the resource</strong>
 *
 *         <p>
 *              Open the appropriate property file and enter a key/value. Lets use a color as an example:
 *              <div><code>com.acidblue.alert.same.name=#FF0000</code></div>
 *         </p>
 *    </li>
 *
 *    <li>
 *         <strong>Obtain access to the resource bundle</strong>
 *          <p>
 *           A developer might load the resource bundle like:
 *              <div>
 *                  <code>
 *                  ResourceBundle  bundle =
 *                      ResourceBundle.getBundle("com.acidblue.pfl.ui.resources.acidblue");
 *                  </code>
 *              </div>
 *
 *          </p>
 *    </li>
 *
 *    <li>
 *         <strong>Attempt to load the resource's value</strong> (note how now the key is hidden in the class,
 *              we have no way of knowing that this key exists until runtime and we may find out by getting
 *              a null pointer exception when the key isn't defined in the resource bundle.  Or we can depend
 *              on the developer to fully document each and every needed resource key in javadoc.
 *         <p>
 *             <code>String sameNameAlertBundleValue = bundle.getString("com.acidblue.alert.same.name");</code>
 *         </p>
 *
 *    </li>
 *
 *    <li>
 *         <strong>Define the object type required</strong>
 *          <p>
 *            <code>Color sameNameAlertColor;</code>
 *          </p>
 *    </li>
 *
 *    <li>
 *         <strong>Check the value of the resource and assign</strong>
 *
 *          <p><code><pre>
 *if (sameNameAlertBundleValue != null) {
 *    sameNameAlertColor = java.awt.Color.decode(sameNameAlertBundleValue);
 *}
 *          </code></pre></p>
 *
 *    </li>
 *    <li>
 *         <strong>If the value was not provided, we need a default</strong> (hopefully the default was set)
 *          <p><code>
 *              sameNameAlertBundleValue = java.awt.Color.decode("com.acidblue.alert.default");
 *          </code></p>
 *    </li>
 *
 *     <li>
 *         <strong>If the value was not provided, we need a default</strong> (hopefully the default was set)
 *          <p><code>
 *              sameNameAlertBundleValue = java.awt.Color.decode("com.acidblue.alert.default");
 *          </code></p>
 *    </li>
 *
 * </ol>
 *
 * </p>
 *
 *
 * </p>
 *
 * <p>
 *
 * The acidblue API provides a more robust solution.
 *
 *
 * </p>
 *
 * <p>
 * There are 3 annoations and one factory that a developer needs to focus on:
 * <ul>
 *  <li>
 *      <dl>
 *        <dt>{@link com.acidblue.transformer.resource.Resource @Resource}</dt>
 *        <dd>This annotationprocessor is required to mark an interface as a 'resourceable' interface.  This allows
 *            for the annotationprocessor processor to locate interfaces that contain resource bundle values. The
 *            annotationprocessor processor then interrogate the interface for the other (@Default, @DefaultParam)
 *            annotations to build the resource bundle.</dd>
 *      </dl>
 * </li>
 * <li>
 *      <dl>
 *        <dt>{@link com.acidblue.transformer.resource.Default @Default}</dt>
 *        <dd>This annotationprocessor marks an abstract method within an interface to be marked as a resource
 *            and provides that resource with a default value.  This will be read by the
 *            annoation processor when creating the resource bundle.</dd>
 *      </dl>
 * </li>
 * <li>
 *      <dl>
 *        <dt>{@link com.acidblue.transformer.resource.DefaultParam @DefaultParam}</dt>
 *        <dd>This annotationprocessor allows for methods, which take a single parameter, to provide default
 *            a resourced value for the value of the paramter.  See the
 *            {@link com.acidblue.transformer.resource.DefaultParam javadoc} javadoc more information.
 *        </dd>
 *      </dl>
 * </li>
 *
 * </ul>
 *
 * </p>
 *
 * <h3>Defining a resource</h3>
 *
 * <ol>
 *    <li>
 *          <strong>Define the resource interface</strong>
 *          <p>
 * <code><pre>
 * package mypackage;
 *
 * {@code @Resource("An example resource interface")
 * public interface MyResourceInterface {
 *
 *    Default("#FF0000");
 *    Color sameNameAlertColor();
 * }
 *
 * </pre>}
 *          </p>
 * </li>
 *
 * <li>
 *   <strong>Obtain an instance of the interface within the necessary class</strong>
 *   <p>
 *      final MyResourceInterface resources = ResolverFactory.get(MyResourceInterface.class);
 *   </p>
 *
 * </li>
 * <li>
 *   <strong>Obtain the value</strong>
 *   <p>
 *   final Color sameNameAlertColor = resources.sameNameAlertColor();
 *   </p>
 * </li>
 * </ol>
 *
 *
 *
 *
 *
 *
 */
package com.acidblue.transformer.resource;