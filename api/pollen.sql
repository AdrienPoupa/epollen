CREATE TABLE departments
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    number VARCHAR(10) NOT NULL
);

CREATE TABLE trees
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    number INT(11) NOT NULL
);

CREATE TABLE risks
(
    id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
    risk FLOAT NOT NULL,
    departments_id INT(11) NOT NULL,
    trees_id INT(11) NOT NULL,
    CONSTRAINT risks_departments_id_fk FOREIGN KEY (departments_id) REFERENCES departments (id),
    CONSTRAINT risks_trees_id_fk FOREIGN KEY (trees_id) REFERENCES trees (id)
);

CREATE INDEX risks_departments_id_fk ON risks (departments_id);
CREATE INDEX risks_trees_id_fk ON risks (trees_id);

INSERT INTO pollen.departments (name, number) VALUES ('Ain', '1');
INSERT INTO pollen.departments (name, number) VALUES ('Aisne', '2');
INSERT INTO pollen.departments (name, number) VALUES ('Allier', '3');
INSERT INTO pollen.departments (name, number) VALUES ('Alpes de Haute Provence', '4');
INSERT INTO pollen.departments (name, number) VALUES ('Hautes-Alpes', '5');
INSERT INTO pollen.departments (name, number) VALUES ('Alpes-Maritimes', '6');
INSERT INTO pollen.departments (name, number) VALUES ('Ardèches', '7');
INSERT INTO pollen.departments (name, number) VALUES ('Ardennes', '8');
INSERT INTO pollen.departments (name, number) VALUES ('Arièges', '9');
INSERT INTO pollen.departments (name, number) VALUES ('Aube', '10');
INSERT INTO pollen.departments (name, number) VALUES ('Aude', '11');
INSERT INTO pollen.departments (name, number) VALUES ('Aveyron', '12');
INSERT INTO pollen.departments (name, number) VALUES ('Bouche du Rhône', '13');
INSERT INTO pollen.departments (name, number) VALUES ('Calvados', '14');
INSERT INTO pollen.departments (name, number) VALUES ('Cantal', '15');
INSERT INTO pollen.departments (name, number) VALUES ('Charente', '16');
INSERT INTO pollen.departments (name, number) VALUES ('Charente Maritime', '17');
INSERT INTO pollen.departments (name, number) VALUES ('Cher', '18');
INSERT INTO pollen.departments (name, number) VALUES ('Corrèze', '19');
INSERT INTO pollen.departments (name, number) VALUES ('Corse', '20');
INSERT INTO pollen.departments (name, number) VALUES ('Côte d''Or', '21');
INSERT INTO pollen.departments (name, number) VALUES ('Côte d''Armor', '22');
INSERT INTO pollen.departments (name, number) VALUES ('Creuse', '23');
INSERT INTO pollen.departments (name, number) VALUES ('Dordogne', '24');
INSERT INTO pollen.departments (name, number) VALUES ('Doubs', '25');
INSERT INTO pollen.departments (name, number) VALUES ('Drôme', '26');
INSERT INTO pollen.departments (name, number) VALUES ('Eure', '27');
INSERT INTO pollen.departments (name, number) VALUES ('Eure et Loire', '28');
INSERT INTO pollen.departments (name, number) VALUES ('Finistère', '29');
INSERT INTO pollen.departments (name, number) VALUES ('Gard', '30');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Garonne', '31');
INSERT INTO pollen.departments (name, number) VALUES ('Gers', '32');
INSERT INTO pollen.departments (name, number) VALUES ('Gironde', '33');
INSERT INTO pollen.departments (name, number) VALUES ('Hérault', '34');
INSERT INTO pollen.departments (name, number) VALUES ('Ille et Vilaine', '35');
INSERT INTO pollen.departments (name, number) VALUES ('Indre', '36');
INSERT INTO pollen.departments (name, number) VALUES ('Indre et Loire', '37');
INSERT INTO pollen.departments (name, number) VALUES ('Isère', '38');
INSERT INTO pollen.departments (name, number) VALUES ('Jura', '39');
INSERT INTO pollen.departments (name, number) VALUES ('Landes', '40');
INSERT INTO pollen.departments (name, number) VALUES ('Loir et Cher', '41');
INSERT INTO pollen.departments (name, number) VALUES ('Loire', '42');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Loire', '43');
INSERT INTO pollen.departments (name, number) VALUES ('Loire Atlantique', '44');
INSERT INTO pollen.departments (name, number) VALUES ('Loiret', '45');
INSERT INTO pollen.departments (name, number) VALUES ('Lot', '46');
INSERT INTO pollen.departments (name, number) VALUES ('Lot et Garonne', '47');
INSERT INTO pollen.departments (name, number) VALUES ('Lozère', '48');
INSERT INTO pollen.departments (name, number) VALUES ('Maine et Loire', '49');
INSERT INTO pollen.departments (name, number) VALUES ('Manche', '50');
INSERT INTO pollen.departments (name, number) VALUES ('Marne', '51');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Marne', '52');
INSERT INTO pollen.departments (name, number) VALUES ('Mayenne', '53');
INSERT INTO pollen.departments (name, number) VALUES ('Meurthe et Moselle', '54');
INSERT INTO pollen.departments (name, number) VALUES ('Meuse', '55');
INSERT INTO pollen.departments (name, number) VALUES ('Morbihan', '56');
INSERT INTO pollen.departments (name, number) VALUES ('Moselle', '57');
INSERT INTO pollen.departments (name, number) VALUES ('Nièvre', '58');
INSERT INTO pollen.departments (name, number) VALUES ('Nord', '59');
INSERT INTO pollen.departments (name, number) VALUES ('Oise', '60');
INSERT INTO pollen.departments (name, number) VALUES ('Orne', '61');
INSERT INTO pollen.departments (name, number) VALUES ('Pas de Calais', '62');
INSERT INTO pollen.departments (name, number) VALUES ('Puy de Dôme', '63');
INSERT INTO pollen.departments (name, number) VALUES ('Pyrénées Atlantiques', '64');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Pyrénées', '65');
INSERT INTO pollen.departments (name, number) VALUES ('Pyrénées Orientales', '66');
INSERT INTO pollen.departments (name, number) VALUES ('Bas Rhin', '67');
INSERT INTO pollen.departments (name, number) VALUES ('Haut Rhin', '68');
INSERT INTO pollen.departments (name, number) VALUES ('Rhône', '69');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Saône', '70');
INSERT INTO pollen.departments (name, number) VALUES ('Saône et Loire', '71');
INSERT INTO pollen.departments (name, number) VALUES ('Sarthe', '72');
INSERT INTO pollen.departments (name, number) VALUES ('Savoie', '73');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Savoie', '74');
INSERT INTO pollen.departments (name, number) VALUES ('Paris', '75');
INSERT INTO pollen.departments (name, number) VALUES ('Seine Maritime', '76');
INSERT INTO pollen.departments (name, number) VALUES ('Seine et Marne', '77');
INSERT INTO pollen.departments (name, number) VALUES ('Yvelines', '78');
INSERT INTO pollen.departments (name, number) VALUES ('Deux Sèvres', '79');
INSERT INTO pollen.departments (name, number) VALUES ('Somme', '80');
INSERT INTO pollen.departments (name, number) VALUES ('Tarn', '81');
INSERT INTO pollen.departments (name, number) VALUES ('Tarn et Garonne', '82');
INSERT INTO pollen.departments (name, number) VALUES ('Var', '83');
INSERT INTO pollen.departments (name, number) VALUES ('Vaucluse', '84');
INSERT INTO pollen.departments (name, number) VALUES ('Vendée', '85');
INSERT INTO pollen.departments (name, number) VALUES ('Vienne', '86');
INSERT INTO pollen.departments (name, number) VALUES ('Haute Vienne', '87');
INSERT INTO pollen.departments (name, number) VALUES ('Vosges', '88');
INSERT INTO pollen.departments (name, number) VALUES ('Yonne', '89');
INSERT INTO pollen.departments (name, number) VALUES ('Territoire de Belfort', '90');
INSERT INTO pollen.departments (name, number) VALUES ('Essonne', '91');
INSERT INTO pollen.departments (name, number) VALUES ('Hauts de Seine', '92');
INSERT INTO pollen.departments (name, number) VALUES ('Seine Saint Denis', '93');
INSERT INTO pollen.departments (name, number) VALUES ('Val de Marne', '94');
INSERT INTO pollen.departments (name, number) VALUES ('Val d''Oise', '95');
INSERT INTO pollen.departments (name, number) VALUES ('Andore', '99');

INSERT INTO pollen.trees (name, number) VALUES ('Cupressacées', '01');
INSERT INTO pollen.trees (name, number) VALUES ('Noisetier', '02');
INSERT INTO pollen.trees (name, number) VALUES ('Aulne', '03');
INSERT INTO pollen.trees (name, number) VALUES ('Peuplier', '04');
INSERT INTO pollen.trees (name, number) VALUES ('Saule', '05');
INSERT INTO pollen.trees (name, number) VALUES ('Frêne', '06');
INSERT INTO pollen.trees (name, number) VALUES ('Charme', '07');
INSERT INTO pollen.trees (name, number) VALUES ('Bouleau', '08');
INSERT INTO pollen.trees (name, number) VALUES ('Platane', '09');
INSERT INTO pollen.trees (name, number) VALUES ('Chêne', '10');
INSERT INTO pollen.trees (name, number) VALUES ('Olivier', '11');
INSERT INTO pollen.trees (name, number) VALUES ('Tilleul', '12');
INSERT INTO pollen.trees (name, number) VALUES ('Châtaignier', '13');
INSERT INTO pollen.trees (name, number) VALUES ('Rumex', '14');
INSERT INTO pollen.trees (name, number) VALUES ('Graminées', '15');
INSERT INTO pollen.trees (name, number) VALUES ('Plantain', '16');
INSERT INTO pollen.trees (name, number) VALUES ('Urticacées', '17');
INSERT INTO pollen.trees (name, number) VALUES ('Armoises', '18');
INSERT INTO pollen.trees (name, number) VALUES ('Ambroises', '19');

INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 1, 1);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 1, 2);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 1, 3);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 1, 4);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 1, 5);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 1, 6);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 1, 7);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 1, 8);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 1, 9);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 1, 10);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 1, 11);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 1, 12);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 1, 13);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 1, 14);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 1, 15);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 1, 16);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 1, 17);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 1, 18);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 1, 19);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 2, 1);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 2, 2);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 2, 3);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 2, 4);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 2, 5);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 2, 6);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 2, 7);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 2, 8);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 2, 9);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 2, 10);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 2, 11);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 2, 12);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 2, 13);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 2, 14);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (5, 2, 15);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (1, 2, 16);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (2, 2, 17);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (3, 2, 18);
INSERT INTO pollen.risks (risk, department_id, tree_id) VALUES (4, 2, 19);