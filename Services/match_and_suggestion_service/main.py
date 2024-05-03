from fastapi import FastAPI, HTTPException
import app.service.service as service

# uvicorn main:app --reload
app = FastAPI()


@app.get("/api/match-service/match/users/by-tags")
async def match_users_by_tags(uid: str):
    if not uid:
        raise HTTPException(status_code=400, detail="ID is required")

    return await service.match_users_by_user_id_for_user_tags(uid)


@app.get("/api/match-service/match/users/by-education")
async def match_users_by_education(uid: str):
    if not uid:
        raise HTTPException(status_code=400, detail="ID is required")

    return await service.match_users_by_user_id_for_education(uid)


@app.get("/api/match-service/match/users/by-experience")
async def match_users_by_experience(uid: str):
    if not uid:
        raise HTTPException(status_code=400, detail="ID is required")

    return await service.match_users_by_user_id_for_experience(uid)


@app.get("/api/match-service/suggest/projects")
async def suggest_projects(uid: str):
    if not uid:
        raise HTTPException(status_code=400, detail="ID is required")
    return await service.suggest_projects_by_user_id_and_project_id(uid)


@app.get("/api/match-service/match/users")
async def match_users_by_education_and_experience_and_tags(uid: str):
    if not uid:
        raise HTTPException(status_code=400, detail="ID is required")

    return await service.match_users_by_education_and_experience_and_tags(uid)
