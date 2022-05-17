export class RulesResponse {
  constructor(
    public message: string = '',
    public timestamp: Date = new Date(),
    public refresh: string[] = []
  ) {}
}
