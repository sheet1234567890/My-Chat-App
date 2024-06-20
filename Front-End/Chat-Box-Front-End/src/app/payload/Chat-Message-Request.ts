import { FileData } from "./File-Data-Request";

export class ChatMessageRequest
{
    id=0;
    message='';
    senderId=0;
    receiverId=0;
    file:FileData[]=[];
}