import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Post } from '../model/post';

@Injectable({
  providedIn: 'root',
})
export class PostServiceService {
  url = 'http://localhost:8080/post';

  constructor(private _http: HttpClient) {}

  getAll() {
    const url = this.url;
    return this._http.get<any>(url);
  }

  getById(id: String) {
    const url = this.url + '/' + id;
    return this._http.get<any>(url);
  }

  likePost(userId: String, postId: String) {
    const url = this.url + '/like/' + userId + '/' + postId;
    return this._http.put<any>(url, null);
  }

  dislikePost(userId: String, postId: String) {
    const url = this.url + '/dislike/' + userId + '/' + postId;
    return this._http.put<any>(url, null);
  }

  reportPost(userId: String, postId: String) {
    const url = this.url + '/report/' + userId + '/' + postId;
    return this._http.put<any>(url, null);
  }

  createPost(post: Post) {
    const url = this.url;
    return this._http.post<any>(url, post);
  }
}
