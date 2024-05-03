from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base

USER_DB = "mysql+mysqlconnector://root:12345678@localhost/cop_authentication_db"
PROJECT_DB = "mysql+mysqlconnector://root:12345678@localhost/cop_project_db"

userDbEngine = create_engine(USER_DB)
projectDbEngine = create_engine(PROJECT_DB)

Base = declarative_base()
