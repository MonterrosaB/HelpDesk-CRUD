CREATE TABLE Usuario(
IdUsuario varchar2(60) PRIMARY KEY,
Nombre varchar2(50) NOT NULL,
Email varchar2(100) NOT NULL,
NUser varchar2(50) NOT NULL,
Contraseña varchar2(50) NOT NULL
)
CREATE TABLE Ticket (
IdTicket varchar2(60) PRIMARY KEY ,
Titulo varchar(20) NOT NULL,
Descripcion varchar2(150) NOT NULL,
Estado CHAR(1) CHECK (Estado IN ('A', 'F')) NOT NULL,
IdUsuario varchar2(60) NOT NULL,
CONSTRAINT FK_Usuario FOREIGN KEY (IdUsuario) REFERENCES Usuario(IdUsuario)
);