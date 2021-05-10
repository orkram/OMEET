export enum Status{
  Active = 'Active',
  Inactive = 'Inactive'
}

export class Meeting {
  id: string;
  title: string;
  status: Status = Status.Inactive;
  listOfParticipants: string[] = [];
  description = 'Short meeting description(Optional)';
  date = new Date().toISOString();

  constructor(id: string, title: string, status: Status) {
    this.id = id;
    this.title = title;
    this.status = status;
  }
}
