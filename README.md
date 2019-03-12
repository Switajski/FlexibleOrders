FlexibleOrders <a href="https://travis-ci.org/Switajski/FlexibleOrders">![Build Status](https://travis-ci.org/Switajski/FlexibleOrders.svg)</a> 
==============

FlexibleOrders is a simple java web application for order fulfilment in B2B: creating and tracking orders, invoices, shipping info, etc.  
Intention of this application is to ease work of young companies by a simple user interface and ability to integrate with e-commerce systems. FlexibleOrders has no product catalog and no CRM.

Limitations of existing order fulfilment solutions, that I know are:
 - Order Fulfilment often part of whole ERP-System
 - Creation of delivery notes, that reflect partial delivery.
 - Tracking of pending (partial) deliveries

Used technologies are Spring-boot and ExtJs in GUI.

Getting started
---------------
Run from command line: 

`git clone git@github.com:Switajski/FlexibleOrders.git
 docker-compose up -d
 mvn spring-boot:run`

Contribute
----------
All pull requests welcome!

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
