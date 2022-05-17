export class Post {
  constructor(
    public postId: string = '',
    public postOwnerId: string = '',
    public postContent: string = '',
    public timestamp: Date = new Date(),
    public likes: number = 0,
    public dislikes: number = 0,
    public reports: number = 0,
    public postLabels: string[] = []
  ) {}
}
