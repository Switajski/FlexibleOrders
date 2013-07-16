FlexibleOrders
==============

FlexibleOrders is a java web application for maintaining and printing orders, invoices, shipping info, etc.
The intention of this application is the basic mapping of one process in java using (latest) state-of-the-art techniques.
Used technologies are Spring, Hibernate and ExtJs as GUI. Core Elements are Spring-Data's "Repositories" and "Services".
The domain model is depicted with POJOs.


The purchasing process
----------------------
An order consists of order items. These items are passing though States (Status) as shown below:

1. new OrderItem -> `Status.ORDERED`
2. OrderConfirmationItem.confirm -> `Status.CONFIRMED`
3. ShippingItem.deliver -> `Status.SHIPPED`
4. ArchiveItem.account -> `Status.COMPLETED`

    *One feature of this application is to provide the process easy modifiable to any (IT)-users so they can be free to modify this application for their own needs.*

Process depicted
----------------
Each state has equivalent tables with items as rows/objects (e.g. OrderItem, ShippingItem, ...). 
The reports (invoices, orders, ...) are generated dynamically by finding items by orderNumber/invoiceNumber etc. 
This report generation enables piecemeal delivering and flexibility in changing reports.

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
