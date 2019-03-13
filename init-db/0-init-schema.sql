--
-- PostgreSQL database dump
--

-- Dumped from database version 11.1 (Debian 11.1-1.pgdg90+1)
-- Dumped by pg_dump version 11.1 (Debian 11.1-1.pgdg90+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: c_order; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.c_order (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    customer_email character varying(255),
    order_number character varying(255) NOT NULL,
    origin_system integer,
    pa_customer_number bigint,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    delivery_type integer,
    external_id bigint,
    name character varying(255),
    phone character varying(255),
    expected_delivery date,
    invoice_city character varying(255),
    invoice_country character varying(255),
    invoice_name1 character varying(255),
    invoice_name2 character varying(255),
    invoice_postal_code integer,
    invoice_street character varying(255),
    pa_payment_conditions character varying(255),
    shipping_city character varying(255),
    shipping_country character varying(255),
    shipping_name1 character varying(255),
    shipping_name2 character varying(255),
    shipping_postal_code integer,
    shipping_street character varying(255),
    vat_rate double precision NOT NULL,
    customer_id bigint
);


ALTER TABLE public.c_order OWNER TO dukturala;

--
-- Name: cancel_report; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.cancel_report (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL
);


ALTER TABLE public.cancel_report OWNER TO dukturala;

--
-- Name: catalog_delivery_method; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.catalog_delivery_method (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    delivery_type integer,
    external_id bigint,
    name character varying(255),
    phone character varying(255)
);


ALTER TABLE public.catalog_delivery_method OWNER TO dukturala;

--
-- Name: catalog_product; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.catalog_product (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    active boolean NOT NULL,
    image_galery character varying(255),
    name character varying(255) NOT NULL,
    product_number character varying(255) NOT NULL,
    product_type integer NOT NULL,
    currency integer,
    value numeric(19,2),
    sort_order bigint,
    category_id bigint
);


ALTER TABLE public.catalog_product OWNER TO dukturala;

--
-- Name: category; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    activated boolean NOT NULL,
    description character varying(255),
    image character varying(255),
    image_galery character varying(255),
    intro character varying(255),
    name character varying(255) NOT NULL,
    sort_order integer NOT NULL
);


ALTER TABLE public.category OWNER TO dukturala;

--
-- Name: category_child_categories; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.category_child_categories (
    category_id bigint NOT NULL,
    child_categories_id bigint NOT NULL
);


ALTER TABLE public.category_child_categories OWNER TO dukturala;

--
-- Name: credit_note; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.credit_note (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL
);


ALTER TABLE public.credit_note OWNER TO dukturala;

--
-- Name: customer; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.customer (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    company_name character varying(255),
    customer_number bigint NOT NULL,
    contact1 character varying(255),
    contact2 character varying(255),
    contact3 character varying(255),
    contact4 character varying(255),
    mark character varying(255),
    payment_conditions character varying(255),
    sale_representative character varying(255),
    vat_id_no character varying(255),
    vendor_number character varying(255),
    email character varying(255),
    fax character varying(255),
    first_name character varying(255),
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    last_name character varying(255),
    notes text,
    payment_grace_period integer NOT NULL,
    phone character varying(255),
    shipping_city character varying(255),
    shipping_country character varying(255),
    shipping_name1 character varying(255),
    shipping_name2 character varying(255),
    shipping_postal_code integer,
    shipping_street character varying(255)
);


ALTER TABLE public.customer OWNER TO dukturala;

--
-- Name: delivery_notes; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.delivery_notes (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    delivery_type integer,
    external_id bigint,
    name character varying(255),
    phone character varying(255),
    package_number character varying(255),
    shipped_city character varying(255),
    shipped_country character varying(255),
    shipped_name1 character varying(255),
    shipped_name2 character varying(255),
    shipped_postal_code integer,
    shipped_street character varying(255),
    currency integer,
    value numeric(19,2),
    show_prices boolean,
    track_number character varying(255)
);


ALTER TABLE public.delivery_notes OWNER TO dukturala;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: dukturala
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO dukturala;

--
-- Name: invoice; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.invoice (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL,
    billing character varying(255),
    discount_rate numeric(19,2),
    discount_text character varying(255),
    evaluation_date timestamp without time zone,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    currency integer,
    value numeric(19,2)
);


ALTER TABLE public.invoice OWNER TO dukturala;

--
-- Name: order_confirmation; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.order_confirmation (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL,
    contact1 character varying(255),
    contact2 character varying(255),
    contact3 character varying(255),
    contact4 character varying(255),
    mark character varying(255),
    payment_conditions character varying(255),
    sale_representative character varying(255),
    vat_id_no character varying(255),
    vendor_number character varying(255),
    order_agreement_number character varying(255),
    pa_customer_number bigint,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    delivery_type integer,
    external_id bigint,
    name character varying(255),
    phone character varying(255),
    expected_delivery date,
    invoice_city character varying(255),
    invoice_country character varying(255),
    invoice_name1 character varying(255),
    invoice_name2 character varying(255),
    invoice_postal_code integer,
    invoice_street character varying(255),
    pa_payment_conditions character varying(255),
    shipping_city character varying(255),
    shipping_country character varying(255),
    shipping_name1 character varying(255),
    shipping_name2 character varying(255),
    shipping_postal_code integer,
    shipping_street character varying(255)
);


ALTER TABLE public.order_confirmation OWNER TO dukturala;

--
-- Name: order_item; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.order_item (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    additional_info character varying(255),
    currency integer,
    value numeric(19,2),
    ordered_quantity integer NOT NULL,
    package_number character varying(255),
    name character varying(255),
    no character varying(255),
    type integer,
    tracking_number character varying(255),
    customer_order_id bigint NOT NULL
);


ALTER TABLE public.order_item OWNER TO dukturala;

--
-- Name: purchase_agreement_deviation; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.purchase_agreement_deviation (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    pa_customer_number bigint,
    city character varying(255),
    country character varying(255),
    name1 character varying(255),
    name2 character varying(255),
    postal_code integer,
    street character varying(255),
    delivery_type integer,
    external_id bigint,
    name character varying(255),
    phone character varying(255),
    expected_delivery date,
    invoice_city character varying(255),
    invoice_country character varying(255),
    invoice_name1 character varying(255),
    invoice_name2 character varying(255),
    invoice_postal_code integer,
    invoice_street character varying(255),
    pa_payment_conditions character varying(255),
    shipping_city character varying(255),
    shipping_country character varying(255),
    shipping_name1 character varying(255),
    shipping_name2 character varying(255),
    shipping_postal_code integer,
    shipping_street character varying(255),
    order_confirmation_id bigint NOT NULL
);


ALTER TABLE public.purchase_agreement_deviation OWNER TO dukturala;

--
-- Name: receipt; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.receipt (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    document_number character varying(255) NOT NULL,
    payment_received timestamp without time zone
);


ALTER TABLE public.receipt OWNER TO dukturala;

--
-- Name: report_item; Type: TABLE; Schema: public; Owner: dukturala
--

CREATE TABLE public.report_item (
    dtype character varying(31) NOT NULL,
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    version integer,
    overdue integer,
    quantity integer NOT NULL,
    pending boolean,
    package_number character varying(255),
    track_number character varying(255),
    order_item_id bigint NOT NULL,
    predecessor_id bigint,
    report_id bigint
);


ALTER TABLE public.report_item OWNER TO dukturala;


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: dukturala
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- Name: c_order c_order_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.c_order
    ADD CONSTRAINT c_order_pkey PRIMARY KEY (id);


--
-- Name: cancel_report cancel_report_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.cancel_report
    ADD CONSTRAINT cancel_report_pkey PRIMARY KEY (id);


--
-- Name: catalog_delivery_method catalog_delivery_method_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.catalog_delivery_method
    ADD CONSTRAINT catalog_delivery_method_pkey PRIMARY KEY (id);


--
-- Name: catalog_product catalog_product_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.catalog_product
    ADD CONSTRAINT catalog_product_pkey PRIMARY KEY (id);


--
-- Name: category_child_categories category_child_categories_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category_child_categories
    ADD CONSTRAINT category_child_categories_pkey PRIMARY KEY (category_id, child_categories_id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: credit_note credit_note_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.credit_note
    ADD CONSTRAINT credit_note_pkey PRIMARY KEY (id);


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: delivery_notes delivery_notes_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.delivery_notes
    ADD CONSTRAINT delivery_notes_pkey PRIMARY KEY (id);


--
-- Name: invoice invoice_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT invoice_pkey PRIMARY KEY (id);


--
-- Name: order_confirmation order_confirmation_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.order_confirmation
    ADD CONSTRAINT order_confirmation_pkey PRIMARY KEY (id);


--
-- Name: order_item order_item_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT order_item_pkey PRIMARY KEY (id);


--
-- Name: purchase_agreement_deviation purchase_agreement_deviation_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.purchase_agreement_deviation
    ADD CONSTRAINT purchase_agreement_deviation_pkey PRIMARY KEY (id);


--
-- Name: receipt receipt_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT receipt_pkey PRIMARY KEY (id);


--
-- Name: report_item report_item_pkey; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.report_item
    ADD CONSTRAINT report_item_pkey PRIMARY KEY (id);


--
-- Name: category uk_46ccwnsi9409t36lurvtyljak; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT uk_46ccwnsi9409t36lurvtyljak UNIQUE (name);


--
-- Name: c_order uk_7lalmpfq2fb5r3bvpdo4fkj1u; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.c_order
    ADD CONSTRAINT uk_7lalmpfq2fb5r3bvpdo4fkj1u UNIQUE (order_number);


--
-- Name: invoice uk_bvawmt5bhoaua5hrc9xyuwcjx; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.invoice
    ADD CONSTRAINT uk_bvawmt5bhoaua5hrc9xyuwcjx UNIQUE (document_number);


--
-- Name: customer uk_dwk6cx0afu8bs9o4t536v1j5v; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_dwk6cx0afu8bs9o4t536v1j5v UNIQUE (email);


--
-- Name: delivery_notes uk_e81dkrkpkog9i6wocwqo168bc; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.delivery_notes
    ADD CONSTRAINT uk_e81dkrkpkog9i6wocwqo168bc UNIQUE (document_number);


--
-- Name: credit_note uk_f4cdhioq561buo8w2ej4kd4e4; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.credit_note
    ADD CONSTRAINT uk_f4cdhioq561buo8w2ej4kd4e4 UNIQUE (document_number);


--
-- Name: catalog_product uk_ffcteulsb92relolghpv59aym; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.catalog_product
    ADD CONSTRAINT uk_ffcteulsb92relolghpv59aym UNIQUE (product_number);


--
-- Name: cancel_report uk_g3xj3f0wk5do5u9v4gho0rf4l; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.cancel_report
    ADD CONSTRAINT uk_g3xj3f0wk5do5u9v4gho0rf4l UNIQUE (document_number);


--
-- Name: category_child_categories uk_lh0khynpyeapn7anh318os9n6; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category_child_categories
    ADD CONSTRAINT uk_lh0khynpyeapn7anh318os9n6 UNIQUE (child_categories_id);


--
-- Name: customer uk_mo8uc9hxwlycihsxq6ucdxs1c; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk_mo8uc9hxwlycihsxq6ucdxs1c UNIQUE (customer_number);


--
-- Name: receipt uk_sdxmf995csy35scbfge0m5s9y; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.receipt
    ADD CONSTRAINT uk_sdxmf995csy35scbfge0m5s9y UNIQUE (document_number);


--
-- Name: order_confirmation uk_suppiovosvsokia3lovppjfxs; Type: CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.order_confirmation
    ADD CONSTRAINT uk_suppiovosvsokia3lovppjfxs UNIQUE (document_number);


--
-- Name: catalog_product fk14luibcx9hpayy1i8n0vb69yx; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.catalog_product
    ADD CONSTRAINT fk14luibcx9hpayy1i8n0vb69yx FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: order_item fk48mmnt5s1vs084a0nw06edpem; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.order_item
    ADD CONSTRAINT fk48mmnt5s1vs084a0nw06edpem FOREIGN KEY (customer_order_id) REFERENCES public.c_order(id);


--
-- Name: report_item fk590yhp9lnwr6ht1ljkcbonrfv; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.report_item
    ADD CONSTRAINT fk590yhp9lnwr6ht1ljkcbonrfv FOREIGN KEY (predecessor_id) REFERENCES public.report_item(id);


--
-- Name: purchase_agreement_deviation fk61heji2etyciouqgjaudo0kk4; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.purchase_agreement_deviation
    ADD CONSTRAINT fk61heji2etyciouqgjaudo0kk4 FOREIGN KEY (order_confirmation_id) REFERENCES public.order_confirmation(id);


--
-- Name: report_item fk8j45smd4ra3935h7h3wcgdg0p; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.report_item
    ADD CONSTRAINT fk8j45smd4ra3935h7h3wcgdg0p FOREIGN KEY (order_item_id) REFERENCES public.order_item(id);


--
-- Name: category_child_categories fkgc22qjpbgg0l9ih2gt50plcbf; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category_child_categories
    ADD CONSTRAINT fkgc22qjpbgg0l9ih2gt50plcbf FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: c_order fklpfyl57p6a9pwrbcy3wxcg9cl; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.c_order
    ADD CONSTRAINT fklpfyl57p6a9pwrbcy3wxcg9cl FOREIGN KEY (customer_id) REFERENCES public.customer(id);


--
-- Name: category_child_categories fkt2yhl8o0an4k0eoa6fer2pr5; Type: FK CONSTRAINT; Schema: public; Owner: dukturala
--

ALTER TABLE ONLY public.category_child_categories
    ADD CONSTRAINT fkt2yhl8o0an4k0eoa6fer2pr5 FOREIGN KEY (child_categories_id) REFERENCES public.category(id);


--
-- PostgreSQL database dump complete
--

