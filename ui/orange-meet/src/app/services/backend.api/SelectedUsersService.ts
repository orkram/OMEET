export class SelectedUsersService{
  private selectedUsers: Array<string> = [];

  setSelectedUsers(users: Array<string>): void{
    this.selectedUsers = users;
  }

  getSelectedUsers(): Array<string>{
    return this.selectedUsers;
  }

  clear(): void{
    this.selectedUsers = [];
  }
}
