package models

import play.api.mvc.{Request, WrappedRequest}

final case class AuthenticatedRequest[A](
  request: Request[A],
  gpaUtr: Long,
  nominatedCompanyUtr: Long,
  pPeriod: Int,
  pMonthRestriction: Int
) extends WrappedRequest[A](request)
