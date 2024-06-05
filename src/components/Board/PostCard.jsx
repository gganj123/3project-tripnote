import React from 'react';
import profile from '../../assets/profile.png';
import { formmateDate } from '../../utils/date';
import { Link } from 'react-router-dom';
import mockImg from '../../assets/busan.jpg';

const mockHashtag = ['서울', '혼자여행'];

export default function PostCard({ contents }) {
  const { nickname, createdAt, title, content, hashtagResponseDTOList } = contents;
  return (
    <Link
      to={`/post/:postId`}
      className="flex gap-8 items-center border-b border-grey pb-5 mb-4"
    >
      <div className="w-full">
        <div className="flex gap-2 items-center mb-7">
          <img
            src={profile}
            alt="profile image"
            className="w-6 h-6 rounded-full"
          />
          {/* 닉네임 한줄 표시 */}
          <p className="line-clamp-1">{nickname}</p>
          <p className="min-w-fit">{formmateDate(createdAt)}</p>
        </div>
        <h1 className="text-xl">{title}</h1>
        <p className="my-3 leading-6 line-clamp-3 max-sm:hidden md:max-[1100px]:hidden">
          {content}
        </p>
        <div className="flex gap-4 mt-7">
          {(hashtagResponseDTOList || []).map((hashtag) => (
            <span key={hashtag.id} className="text-sm py-1 px-4 rounded-full bg-gray-100">
              #{hashtag.name}
            </span>
          ))}
        </div>
      </div>
      <div className="h-28 aspect-square bg-gray-100">
        <img
          src={mockImg}
          alt=""
          className="w-full h-full aspect-square object-cover"
        />
      </div>
    </Link>
  );
}
