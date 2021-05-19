export class Contact {
  image: string;
  name: string;
  email: string;
  checked = false;

  constructor(image: string, name: string, email: string){
    this.image = image;
    this.name = name;
    this.email = email;
  }
}
