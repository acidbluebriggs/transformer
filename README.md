transformer
===========

A library for annotating methods in interfaces to return either their default value, defined 
in the annotation's properties, or to load the values from their transformable (marshalled or serialized, etc.) versions
stored in external resources such as: resource bundle files, databases or whatever.

This library currently supports resource bundle files.

There is an annotation processor which can inspect all interfaces containing the annotations and will generate a default
resource bundle file containing all the corresponding keys and values as well as their documentation string.

This is something like NSLocalizedString macros found in Cocoa and Cocoa touch libraries only it is not a macro.
