export enum Status{
  Active = 'Active',
  Inactive = 'Inactive'
}

export class Meeting {
  idMeeting: string;
  name: string;
  status: Status = Status.Inactive;
  listOfParticipants: string[] = [];
  description = 'Short meeting description(Optional)';
  date = new Date().toISOString();
  roomUrl = '';

  constructor(id: string, title: string, status: Status, roomUrl: string) {
    this.idMeeting = id;
    this.name = title;
    this.status = status;
    this.roomUrl = roomUrl;
  }
}
