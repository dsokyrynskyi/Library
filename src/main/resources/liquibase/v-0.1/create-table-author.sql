CREATE TABLE Author
(
    author_id integer NOT NULL,
    name character varying(200) NOT NULL,
    birth_date date,
    CONSTRAINT Author_pkey PRIMARY KEY (author_id)
);
ALTER TABLE Author
    OWNER to postgres;