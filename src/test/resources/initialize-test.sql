INSERT INTO client (login, password)
VALUES ('valentine', '{bcrypt}$2a$10$SaDBEsf8xdO9YBu/g27Zde2C5xDVePV.HX3a1aihq958WtwCL0B1S'), --pass:password
       ('dimitrius', '{bcrypt}$2a$10$t0lnFAcnNuJngWH/jJr1CepRENoUkCzROBrPYp/ZvHc13MdlXV38.'); --pass:newpass

INSERT INTO client (login, password, enabled)
VALUES ('disabledclient', '{bcrypt}$2a$10$t0lnFAcnNuJngWH/jJr1CepRENoUkCzROBrPYp/ZvHc13MdlXV38.', false);