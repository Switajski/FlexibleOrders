FlexibleOrders
==============

FlexibleOrders is a java web application for maintaining and printing orders, invoices, shipping info, etc.
The intention of this application is the basic mapping of one process in java using (latest) state-of-the-art techniques.
Used technologies are Spring, Hibernate and ExtJs as GUI. Core Elements are Spring-Data's "Repositories" and "Services".
The domain model is depicted with POJOs.


Domain model - the purchasing process
-------------------------------------
An `Order` consists of `OrderItems`. Flexible Order assigns `ReportItem`s to those `OrderItems`. 
Report Items can be:

- `ConfirmationItem` as part of a `Confirmation Report`
- `ShippingItem` as part of `DeliveryNotes`
- `InvoiceItem` as part of a `Invoice`
- `ReceiptItem` as part of a `Receipt`

    *One feature of this application is to provide the process easy modifiable to any (IT)-users so they can be free to modify this application for their own needs.*

Process depicted
----------------
Each report item represents a state of a purchasing process.  
This report generation enables piecemeal delivering and flexibility in changing reports.

Getting started
---------------
FlexibleOrders is developed with Eclipse with Spring Tool Suite (STS). In order to program it run from command line: 

`git clone git@github.com:Switajski/FlexibleOrders.git`

`mvn eclipse:eclipse`

and import the just created git repository as "existing project" to eclipse. The database can be created by modifying persistence.xml and changing the line:
`<property name="hibernate.hbm2ddl.auto" value="update"/>`
to 
`<property name="hibernate.hbm2ddl.auto" value="create"/>`
and start the application server with a running DB. Postgres is preconfigured.

Contribute
----------
Just send me an email to marek@switajski.de

License
-------
This application is using ExtJs. The licensing model of this application is bound to ExtJs open source license (GPL license v3 - http://www.sencha.com/products/extjs/license/)

    Copyright 2012 Markus Switajski
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
