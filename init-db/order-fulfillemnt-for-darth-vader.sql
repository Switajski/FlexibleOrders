INSERT INTO public.customer (id, created, version, company_name, customer_number, contact1, contact2, contact3, contact4, mark, payment_conditions, sale_representative, vat_id_no, vendor_number, email, fax, first_name, city, country, name1, name2, postal_code, street, last_name, notes, payment_grace_period, phone, shipping_city, shipping_country, shipping_name1, shipping_name2, shipping_postal_code, shipping_street) VALUES (1, '2019-03-13 11:13:33.381000', 0, 'Imperiium', 666, null, null, null, null, null, null, null, null, null, 'Darth@vader.dv', null, 'Darth', 'City', 'IE', 'Darth', 'Vader', 666, 'Center', 'Vader', null, 0, null, 'City', 'IE', 'Darth', 'Vader', 666, 'Center');

INSERT INTO public.c_order (id, created, version, customer_email, order_number, origin_system, pa_customer_number, city, country, name1, name2, postal_code, street, delivery_type, external_id, name, phone, expected_delivery, invoice_city, invoice_country, invoice_name1, invoice_name2, invoice_postal_code, invoice_street, pa_payment_conditions, shipping_city, shipping_country, shipping_name1, shipping_name2, shipping_postal_code, shipping_street, vat_rate, customer_id)
  VALUES (2, '2019-03-13 11:14:09.621000', 0, 'Darth@vader.dv', 'B1903001', 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.19, 1);
INSERT INTO public.order_item (id, created, version, additional_info, currency, value, ordered_quantity, package_number, name, no, type, tracking_number, customer_order_id)
  VALUES (3, '2019-03-13 11:14:09.621000', 0, null, 0, 0.20, 1000000, null, 'Ammo', null, 1, null, 2);

INSERT INTO public.c_order (id, created, version, customer_email, order_number, origin_system, pa_customer_number, city, country, name1, name2, postal_code, street, delivery_type, external_id, name, phone, expected_delivery, invoice_city, invoice_country, invoice_name1, invoice_name2, invoice_postal_code, invoice_street, pa_payment_conditions, shipping_city, shipping_country, shipping_name1, shipping_name2, shipping_postal_code, shipping_street, vat_rate, customer_id)
  VALUES (4, '2019-03-13 11:14:57.052000', 0, 'Darth@vader.dv', 'B1903002', 2, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0.19, 1);
INSERT INTO public.order_item (id, created, version, additional_info, currency, value, ordered_quantity, package_number, name, no, type, tracking_number, customer_order_id)
  VALUES (5, '2019-03-13 11:14:57.052000', 0, null, 0, 21341.00, 1, null, 'Light Sword', null, 1, null, 4);
INSERT INTO public.order_item (id, created, version, additional_info, currency, value, ordered_quantity, package_number, name, no, type, tracking_number, customer_order_id)
  VALUES (6, '2019-03-13 11:14:57.052000', 0, null, 0, 278821367.00, 1, null, 'Death Star', null, 1, null, 4);

INSERT INTO public.order_confirmation (id, created, version, document_number, contact1, contact2, contact3, contact4, mark, payment_conditions, sale_representative, vat_id_no, vendor_number, order_agreement_number, pa_customer_number, city, country, name1, name2, postal_code, street, delivery_type, external_id, name, phone, expected_delivery, invoice_city, invoice_country, invoice_name1, invoice_name2, invoice_postal_code, invoice_street, pa_payment_conditions, shipping_city, shipping_country, shipping_name1, shipping_name2, shipping_postal_code, shipping_street)
  VALUES (7, '2019-03-13 11:16:10.721000', 1, 'AB1903002', null, null, null, null, '', null, '', '', '', 'AU1903002', null, null, null, null, null, null, null, null, null, null, null, '2019-03-27', 'City', 'IE', 'Darth', 'Vader', 666, 'Center', null, 'City', 'IE', 'Darth', 'Vader', 666, 'Center');
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ConfirmationItem', 8, '2019-03-13 11:16:10.726000', 0, 1000000, 1000000, false, null, null, 3, null, 7);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ConfirmationItem', 9, '2019-03-13 11:16:10.741000', 1, 0, 1, false, null, null, 5, null, 7);

INSERT INTO public.delivery_notes (id, created, version, document_number, city, country, name1, name2, postal_code, street, delivery_type, external_id, name, phone, package_number, shipped_city, shipped_country, shipped_name1, shipped_name2, shipped_postal_code, shipped_street, currency, value, show_prices, track_number)
  VALUES (10, '2019-03-14 00:00:00.000000', 0, 'L1903002', null, null, null, null, null, null, null, null, null, null, '', 'City', 'IE', 'Darth', 'Vader', 666, 'Center', 0, null, true, null);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 11, '2019-03-13 11:18:51.935000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 12, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 13, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 14, '2019-03-13 11:18:51.939000', 0, 1954, 1954, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 15, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 16, '2019-03-13 11:18:51.948000', 0, 1, 1, null, null, null, 5, 9, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 17, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 18, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('ShippingItem', 19, '2019-03-13 11:18:51.939000', 0, 1953, 1953, null, null, null, 3, 8, 10);

INSERT INTO public.invoice (id, created, version, document_number, billing, discount_rate, discount_text, evaluation_date, city, country, name1, name2, postal_code, street, currency, value)
  VALUES (20, '2019-03-13 00:00:00.000000', 0, 'R1903002', '', null, '', null, 'City', 'IE', 'Darth', 'Vader', 666, 'Center', 0, 0.00);
INSERT INTO public.report_item (dtype, id, created, version, overdue, quantity, pending, package_number, track_number, order_item_id, predecessor_id, report_id)
  VALUES ('InvoiceItem', 21, '2019-03-13 11:20:06.433000', 0, 1, 1, null, null, null, 5, 16, 20);

