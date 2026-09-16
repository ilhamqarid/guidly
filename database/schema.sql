-- Guidly — schéma de base de données
-- Généré à partir de docs/02-modele-donnees.md
-- À affiner en phase backend (index, contraintes supplémentaires, etc.)

CREATE TABLE roles (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL,
  email VARCHAR(150) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role_id INT NOT NULL,
  preferred_language VARCHAR(10),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (role_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) UNIQUE NOT NULL,
  description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE organizations (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  address VARCHAR(255),
  city VARCHAR(100),
  website VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE procedures (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  intent_code VARCHAR(100) UNIQUE NOT NULL,
  category_id INT NOT NULL,
  organization_id INT NOT NULL,
  description TEXT NOT NULL,
  target_users VARCHAR(255),
  estimated_duration VARCHAR(100),
  estimated_fees VARCHAR(100),
  status ENUM('DRAFT','PUBLISHED','ARCHIVED') NOT NULL DEFAULT 'DRAFT',
  last_verified_at DATE NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (organization_id) REFERENCES organizations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE procedure_steps (
  id INT AUTO_INCREMENT PRIMARY KEY,
  procedure_id INT NOT NULL,
  step_number INT NOT NULL,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  required BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE (procedure_id, step_number),
  FOREIGN KEY (procedure_id) REFERENCES procedures(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE documents (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description TEXT,
  type VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE procedure_documents (
  procedure_id INT NOT NULL,
  document_id INT NOT NULL,
  required BOOLEAN NOT NULL DEFAULT TRUE,
  `condition` VARCHAR(255),
  PRIMARY KEY (procedure_id, document_id),
  FOREIGN KEY (procedure_id) REFERENCES procedures(id),
  FOREIGN KEY (document_id) REFERENCES documents(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE sources (
  id INT AUTO_INCREMENT PRIMARY KEY,
  procedure_id INT NOT NULL,
  title VARCHAR(200) NOT NULL,
  url VARCHAR(500) NOT NULL,
  last_verified_at DATE NOT NULL,
  FOREIGN KEY (procedure_id) REFERENCES procedures(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE glossary_terms (
  id INT AUTO_INCREMENT PRIMARY KEY,
  term VARCHAR(150) UNIQUE NOT NULL,
  explanation TEXT NOT NULL,
  source_id INT,
  FOREIGN KEY (source_id) REFERENCES sources(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE glossary_links (
  procedure_id INT NOT NULL,
  glossary_term_id INT NOT NULL,
  PRIMARY KEY (procedure_id, glossary_term_id),
  FOREIGN KEY (procedure_id) REFERENCES procedures(id),
  FOREIGN KEY (glossary_term_id) REFERENCES glossary_terms(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_procedures (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  procedure_id INT NOT NULL,
  status ENUM('IN_PROGRESS','COMPLETED','ABANDONED') NOT NULL DEFAULT 'IN_PROGRESS',
  started_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  completed_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (procedure_id) REFERENCES procedures(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_step_progress (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_procedure_id INT NOT NULL,
  step_id INT NOT NULL,
  completed BOOLEAN NOT NULL DEFAULT FALSE,
  completed_at DATETIME,
  UNIQUE (user_procedure_id, step_id),
  FOREIGN KEY (user_procedure_id) REFERENCES user_procedures(id),
  FOREIGN KEY (step_id) REFERENCES procedure_steps(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE ai_query_logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  input_text TEXT NOT NULL,
  detected_language VARCHAR(10),
  detected_intent VARCHAR(100),
  confidence DECIMAL(4,3),
  matched_procedure_id INT,
  response_time_ms INT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (matched_procedure_id) REFERENCES procedures(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
