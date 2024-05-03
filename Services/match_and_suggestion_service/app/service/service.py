import uuid

from sqlalchemy import text
from sqlalchemy.engine import ResultProxy
from app.db.db import userDbEngine, projectDbEngine
from app.db.nosql import collection
from app.models.project import Project
from app.models.user import User
from app.query_facade import get_all_users, get_all_projects, search_user_by_id
import json
from bson import json_util
import app.service.match_service as match_service


async def find_users():
    users = []
    with userDbEngine.connect() as connection:

        query_text = text(get_all_users())

        result: ResultProxy = connection.execute(query_text)

        for row in result:
            try:
                user_id = str(uuid.UUID(bytes=bytes(row[0]))) if isinstance(row[0], (bytearray, bytes)) and len(
                    row[0]) == 16 else row[0]
                username = row[1]
                educations = row[2].split(",") if row[2] else []
                user_tags = row[3].split(",") if row[3] else []
                experiences = row[4].split(",") if row[4] else []
                user = User(user_id, username, educations, user_tags, experiences)
                users.append(user)
                print(f"User: {user}")

            except Exception as e:
                print(f"Error processing row: {e}")
                print(f"Type of row[0]: {type(row[0])}")
                print(f"Value of row[0]: {row[0]}")
    return users


async def find_user_by_id(uid: str):
    try:
        with userDbEngine.connect() as connection:

            query_text = text(search_user_by_id(uid))

            result: ResultProxy = connection.execute(query_text)

            row = result.fetchone()

            if not row:
                return None

            user_id = row[0].hex() if isinstance(row[0], bytearray) else str(row[0])
            username = row[1]
            educations = row[2].split(",") if row[2] else []
            user_tags = row[3].split(",") if row[3] else []
            experiences = row[4].split(",") if row[4] else []

            return User(user_id, username, educations, user_tags, experiences)

    except Exception as e:
        print(f"Error processing row: {e}")
        return None


async def find_all_projects():
    projects = []
    with projectDbEngine.connect() as connection:

        query_text = text(get_all_projects())

        result: ResultProxy = connection.execute(query_text)

        for row in result:
            user_dict = {}
            try:
                user_dict["project_id"] = str(uuid.UUID(bytes=bytes(row[0]))) if isinstance(row[0],
                                                                                            (bytearray, bytes)) and len(
                    row[0]) == 16 else row[0]
                user_dict["title"] = row[1]
                tags = await find_project_tags(str(user_dict["project_id"]))

                projects.append(Project(user_dict["project_id"], user_dict["title"], tags))

            except Exception as e:
                print(f"Error processing row: {e}")

    return projects


async def find_project_tags(pid: str):
    return [doc.get('tagName') for doc in collection.find({"projectId": uuid.UUID(bytes=uuid.UUID(pid).bytes)})]


async def suggest_projects_by_user_id_and_project_id(user_id: str):
    user = await find_user_by_id(user_id)
    projects = await find_all_projects()
    return await match_service.recommend_projects_for_user(user, projects)


async def match_users_by_user_id_for_education(user_id: str):
    user = await find_user_by_id(user_id)
    remaining_users = [u for u in await find_users() if u.user_id != user.user_id]
    return await match_service.recommend_users_by_universities(user, remaining_users)


async def match_users_by_user_id_for_user_tags(user_id: str):
    user = await find_user_by_id(user_id)
    users = await find_users()
    remaining_users = [u for u in users if u.user_id != user.user_id]
    return await match_service.recommend_users_by_tags(user, remaining_users)


async def match_users_by_user_id_for_experience(user_id: str):
    user = await find_user_by_id(user_id)
    remaining_users = [u for u in await find_users() if u.user_id != user.user_id]
    return await match_service.recommend_users_by_experiences(user, remaining_users)


async def match_users_by_education_and_experience_and_tags(user_id: str):
    user = await find_user_by_id(user_id)
    remaining_users = [u for u in await find_users() if u.user_id != user.user_id]
    return await match_service.recommend_users(user, remaining_users)


def mongo_to_json(data):
    return json.loads(json_util.dumps(data))
