from fastapi import FastAPI
from pydantic import BaseModel
from llm_service import ask_llm

app = FastAPI()

class ChatRequest(BaseModel):
    message: str

@app.post("/chat")
def chat(req: ChatRequest):
    response = ask_llm(req.message)
    return {"response": response}