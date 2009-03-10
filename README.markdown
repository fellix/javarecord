# JavaRecord

Java ORM Like Rails Active Record.

## Features

* Your model must be extends a JavaRecord class, the class provider the atributes of this model, and metodos to save, delete and find the object in the database.
* Customizations using annotations
* Supports JDBC drivers (Only MySQL tested at this moment)
* You can customize the Drivers implementing the Adapter interface
* You can customize the pluralization implementing the Inflector interface

## Annotations

The Avaliable annotations at this time is:
* @BelongsTo : give an array of model class with the class is belong to
* @HasMany :  give an array of model class with the class have many object (a List)
* @Sequence: give the name to use the sequence with generator (only if the Adapter supports)
* @TableName: give another table name to this model object (for default the table name is the class in plural)

## Requirements

* Java 1.6
* Maven 2 (Build and Compile)

## TODO

* Add file to configurations to
* Suports to HasOne
* Customise models avaliable using annotations
* Add model validarions with annotations
